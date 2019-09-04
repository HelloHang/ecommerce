package com.ecommerce.authorize.authserver.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;


public class CustomAuthenticationToken extends AbstractAuthenticationToken
{
	private CustomUserDetails customUserDetails;

	public CustomAuthenticationToken(CustomUserDetails customUserDetails)
	{
		super(null);
		this.customUserDetails = customUserDetails;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials()
	{
		return this.customUserDetails;
	}

	@Override
	public Object getPrincipal()
	{
		return this.customUserDetails.getPassword();
	}
}
