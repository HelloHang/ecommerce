package com.ecommerce.authorize.authserver.provider;

import com.ecommerce.authorize.authserver.security.CustomAuthenticationToken;
import com.ecommerce.authorize.authserver.security.CustomUserDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Component
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
		Map map = checkUsernameAndPassword(userName, password, type);

		String userId = (String) map.get("userId");
		if (StringUtils.isBlank(userId))
		{
			throw new BadCredentialsException("User is not existing.");
		}
		CustomUserDetails customUserDetails = buildCustomUserDetails(userName, password, userId, clientId);
		return new CustomAuthenticationToken(customUserDetails);
	}

	private CustomUserDetails buildCustomUserDetails(String userName, String password, String userId, String clientId)
	{
		CustomUserDetails customUserDetails = new CustomUserDetails.CustomUserDetailsBuilder().withUserName(userName)
			  .withClientId(clientId).withUserId(userId).withPassword(password).bulid();
		return customUserDetails;
	}

	private Map checkUsernameAndPassword(String userName, String password, String type)
	{
		Map<String, Object> map = new HashMap<>();
		map.put("userId", UUID.randomUUID().toString());
		return map;
	}

	@Override
	public boolean supports(Class<?> aClass)
	{
		return true;
	}
}
