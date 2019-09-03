package com.ecommerce.authorize.authserver.security;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Set;
import java.util.UUID;


public class CustomAuthorizationTokenServices implements AuthorizationServerTokenServices, ConsumerTokenServices
{
	private int refreshTokenValiditySeconds = 60 * 60 * 24 * 30; // default 30 days

	private int accessTokenValiditySeconds = 60 * 60 * 12;

	private boolean supportRefreshToken = false;

	private boolean reuseRefreshToken = true;

	private TokenStore tokenStore;

	private ClientDetailsService clientDetailsService;

	private TokenEnhancer tokenEnhancer;

	private AuthenticationManager authenticationManager;

	@Override
	@Transactional
	public OAuth2AccessToken createAccessToken(OAuth2Authentication oAuth2Authentication) throws AuthenticationException
	{
		OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(oAuth2Authentication);
		OAuth2RefreshToken refreshToken;

		if (existingAccessToken != null)
		{
			if (existingAccessToken.getRefreshToken() != null)
			{
				refreshToken = existingAccessToken.getRefreshToken();
				tokenStore.removeRefreshToken(refreshToken);
			}
			tokenStore.removeAccessToken(existingAccessToken);
		}

		refreshToken = createRefreshToken(oAuth2Authentication);

		OAuth2AccessToken accessToken = createAccessToken(oAuth2Authentication, refreshToken);
		if (accessToken != null)
		{
			tokenStore.storeAccessToken(accessToken, oAuth2Authentication);
		}

		refreshToken = accessToken.getRefreshToken();
		if (refreshToken != null)
		{
			tokenStore.storeRefreshToken(refreshToken, oAuth2Authentication);
		}

		return accessToken;
	}

	private OAuth2AccessToken createAccessToken(OAuth2Authentication oAuth2Authentication, OAuth2RefreshToken refreshToken)
	{
		DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
		int validitySeconds = getAccessTokenValiditySeconds(oAuth2Authentication.getOAuth2Request());
		if (validitySeconds > 0)
		{
			token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
		}
		token.setRefreshToken(refreshToken);
		token.setScope(oAuth2Authentication.getOAuth2Request().getScope());
		return tokenEnhancer != null ? tokenEnhancer.enhance(token, oAuth2Authentication) : token;
	}

	private int getAccessTokenValiditySeconds(OAuth2Request oAuth2Request)
	{
		if (clientDetailsService != null)
		{
			ClientDetails clientDetails = clientDetailsService.loadClientByClientId(oAuth2Request.getClientId());
			Integer validitySeconds = clientDetails.getAccessTokenValiditySeconds();
			if (validitySeconds != null)
			{
				return validitySeconds;
			}
		}
		return accessTokenValiditySeconds;
	}

	private OAuth2RefreshToken createRefreshToken(OAuth2Authentication authentication)
	{
		if (!isSupportRefreshToken(authentication.getOAuth2Request()))
		{
			return null;
		}

		int validitySeconds = getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
		String value = UUID.randomUUID().toString();
		if (validitySeconds > 0)
		{
			return new DefaultExpiringOAuth2RefreshToken(value, new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
		}
		return new DefaultOAuth2RefreshToken(value);
	}

	private int getRefreshTokenValiditySeconds(OAuth2Request oAuth2Request)
	{
		if (clientDetailsService != null)
		{
			ClientDetails clientDetails = clientDetailsService.loadClientByClientId(oAuth2Request.getClientId());
			Integer validitySeconds = clientDetails.getRefreshTokenValiditySeconds();
			if (validitySeconds != null)
			{
				return validitySeconds;
			}
		}
		return refreshTokenValiditySeconds;
	}

	private boolean isSupportRefreshToken(OAuth2Request oAuth2Request)
	{
		if (clientDetailsService != null)
		{
			ClientDetails clientDetails = clientDetailsService.loadClientByClientId(oAuth2Request.getClientId());
			return clientDetails.getAuthorizedGrantTypes().contains("refresh_token");
		}
		return supportRefreshToken;
	}

	@Override
	@Transactional(noRollbackFor = { InvalidTokenException.class, InvalidGrantException.class })
	public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest)
		  throws AuthenticationException
	{
		if (!supportRefreshToken)
		{
			throw new InvalidGrantException(String.format("Invalid refresh token: %refreshTokenValue", refreshTokenValue));
		}

		OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(refreshTokenValue);
		if (refreshToken == null)
		{
			throw new InvalidGrantException(String.format("Invalid refresh token: %refreshTokenValue", refreshTokenValue));
		}

		OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(refreshToken);
		if (authenticationManager != null && !authentication.isClientOnly())
		{
			Authentication user = new PreAuthenticatedAuthenticationToken(authentication.getUserAuthentication(), "",
				  authentication.getAuthorities());
			Object details = authentication.getDetails();
			authentication = new OAuth2Authentication(authentication.getOAuth2Request(), user);
			authentication.setDetails(details);
		}
		String clientId = authentication.getOAuth2Request().getClientId();
		if (clientId == null || !StringUtils.equals(clientId, tokenRequest.getClientId()))
		{
			throw new InvalidGrantException(String.format("Wrong client for this refresh token: %s", refreshTokenValue));
		}

		tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);

		if (isExpired(refreshToken))
		{
			tokenStore.removeRefreshToken(refreshToken);
			throw new InvalidTokenException(String.format("Invalid refresh token (expired): %s", refreshTokenValue));
		}

		authentication = createRefreshedAuthentication(authentication, tokenRequest);

		if (!reuseRefreshToken)
		{
			tokenStore.removeRefreshToken(refreshToken);
			refreshToken = createRefreshToken(authentication);
		}

		OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
		tokenStore.storeAccessToken(accessToken, authentication);

		if (!reuseRefreshToken)
		{
			tokenStore.storeRefreshToken(accessToken.getRefreshToken(), authentication);
		}

		return accessToken;
	}

	private OAuth2Authentication createRefreshedAuthentication(OAuth2Authentication authentication, TokenRequest tokenRequest)
	{
		Set<String> scope = tokenRequest.getScope();
		OAuth2Request clientAuth = authentication.getOAuth2Request().refresh(tokenRequest);
		if (!CollectionUtils.isEmpty(scope))
		{
			Set<String> originScope = clientAuth.getScope();
			if (originScope == null || !originScope.containsAll(scope))
			{
				throw new InvalidScopeException(String.format("Unable to narrow the scope of the client authentication to %s."),
					  originScope);
			}
			else
			{
				clientAuth = clientAuth.narrowScope(scope);
			}
		}
		return new OAuth2Authentication(clientAuth, authentication.getUserAuthentication());
	}

	private boolean isExpired(OAuth2RefreshToken refreshToken)
	{
		if (refreshToken instanceof ExpiringOAuth2RefreshToken)
		{
			ExpiringOAuth2RefreshToken expiringToken = (ExpiringOAuth2RefreshToken) refreshToken;
			return expiringToken.getExpiration() == null || System.currentTimeMillis() > expiringToken.getExpiration().getTime();
		}
		return false;
	}

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication oAuth2Authentication)
	{
		return tokenStore.getAccessToken(oAuth2Authentication);
	}

	@Override
	public boolean revokeToken(String tokenValue)
	{
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
		if (accessToken == null)
		{
			return false;
		}
		if (accessToken.getRefreshToken() != null)
		{
			tokenStore.removeRefreshToken(accessToken.getRefreshToken());
		}
		tokenStore.removeAccessToken(accessToken);
		return true;
	}

	public TokenStore getTokenStore()
	{
		return tokenStore;
	}

	public void setTokenStore(TokenStore tokenStore)
	{
		this.tokenStore = tokenStore;
	}

	public void setSupportRefreshToken(boolean supportRefreshToken)
	{
		this.supportRefreshToken = supportRefreshToken;
	}

	public boolean isReuseRefreshToken()
	{
		return reuseRefreshToken;
	}

	public void setReuseRefreshToken(boolean reuseRefreshToken)
	{
		this.reuseRefreshToken = reuseRefreshToken;
	}

	public ClientDetailsService getClientDetailsService()
	{
		return clientDetailsService;
	}

	public void setClientDetailsService(ClientDetailsService clientDetailsService)
	{
		this.clientDetailsService = clientDetailsService;
	}

	public TokenEnhancer getTokenEnhancer()
	{
		return tokenEnhancer;
	}

	public void setTokenEnhancer(TokenEnhancer tokenEnhancer)
	{
		this.tokenEnhancer = tokenEnhancer;
	}

	public AuthenticationManager getAuthenticationManager()
	{
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager)
	{
		this.authenticationManager = authenticationManager;
	}

	public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds)
	{
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}

	public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds)
	{
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}
}
