package com.ecommerce.authorize.authserver.handler;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CustomeLogoutHandler implements LogoutHandler
{
	private static final Logger LOG = LoggerFactory.getLogger(CustomeLogoutHandler.class);

	@Autowired
	private TokenStore tokenStore;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
	{
		String token = request.getHeader("Authorization");

		if (isJwtBearerToken(token))
		{
			token = token.substring(6);
			OAuth2AccessToken existingAccessToken = tokenStore.readAccessToken(token);
			OAuth2RefreshToken refreshToken;
			if (existingAccessToken != null)
			{
				if (existingAccessToken.getRefreshToken() != null)
				{
					LOG.info("Remove refreshToken!", existingAccessToken.getRefreshToken());
					tokenStore.removeRefreshToken(existingAccessToken.getRefreshToken());
				}
				LOG.info("Remove Access Token!", existingAccessToken);
				tokenStore.removeAccessToken(existingAccessToken);
				return;
			}
		}
		throw new BadClientCredentialsException();
	}

	private boolean isJwtBearerToken(String token)
	{
		return StringUtils.countMatches(token, ".") == 2 && StringUtils.startsWithIgnoreCase(token, "bearer");
	}
}
