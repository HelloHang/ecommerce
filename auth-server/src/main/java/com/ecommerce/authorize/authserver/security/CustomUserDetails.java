package com.ecommerce.authorize.authserver.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class CustomUserDetails implements UserDetails
{
	private String userName;

	private String password;

	private boolean enable = true;

	private String userId;

	private String clientId;

	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return authorities;
	}

	@Override
	public String getPassword()
	{
		return password;
	}

	@Override
	public String getUsername()
	{
		return userName;
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}

	@Override
	public boolean isEnabled()
	{
		return enable;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setEnable(boolean enable)
	{
		this.enable = enable;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities)
	{
		this.authorities = authorities;
	}

	public String getUserId()
	{
		return userId;
	}

	public String getClientId()
	{
		return clientId;
	}

	public static class CustomUserDetailsBuilder
	{
		private CustomUserDetails customUserDetails = new CustomUserDetails();

		public CustomUserDetailsBuilder withUserName(String userName)
		{
			customUserDetails.setUserName(userName);
			return this;
		}

		public CustomUserDetailsBuilder withPassword(String password)
		{
			customUserDetails.setPassword(password);
			return this;
		}

		public CustomUserDetailsBuilder withClientId(String clientId)
		{
			customUserDetails.setClientId(clientId);
			return this;
		}

		public CustomUserDetailsBuilder withUserId(String userId)
		{
			customUserDetails.setUserId(userId);
			return this;
		}

		public CustomUserDetails bulid()
		{
			return customUserDetails;
		}

	}
}
