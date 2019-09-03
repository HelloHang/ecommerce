package com.ecommerce.authorize.authserver.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

import java.util.Map;


public class CustomAuthenticationProvider implements AuthenticationProvider
{
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		String userName = authentication.getName();
		Map details = (Map) authentication.getDetails();
		String clientId = (String) details.get("client");
		Assert.hasText(clientId, "Client Id must have value.");
		String type = (String) details.get("type");
		String password = (String) authentication.getCredentials();
		Map map = userClient.checkUsernameAndPassword(userName, password, type);
		return null;
	}

	@Override
	public boolean supports(Class<?> aClass)
	{
		return true;
	}
}
