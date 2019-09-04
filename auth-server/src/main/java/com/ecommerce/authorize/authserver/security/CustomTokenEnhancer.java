package com.ecommerce.authorize.authserver.security;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class CustomTokenEnhancer extends JwtAccessTokenConverter implements Serializable
{

	public static final String TOKEN_USER_ID = "X-AOHO-UserId";
	public static final String TOKEN_CLIENT_ID = "X-AOHO-ClientId";

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication)
	{
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getUserAuthentication().getCredentials();
		authentication.getUserAuthentication().getPrincipal();
		Map<String, Object> info = new HashMap<>();
		info.put(TOKEN_USER_ID, customUserDetails.getUserId());
		DefaultOAuth2AccessToken auth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
		auth2AccessToken.setAdditionalInformation(info);

		OAuth2AccessToken enhancedToken = super.enhance(auth2AccessToken, authentication);
		enhancedToken.getAdditionalInformation().put(TOKEN_CLIENT_ID, customUserDetails.getClientId());
		return enhancedToken;
	}
}
