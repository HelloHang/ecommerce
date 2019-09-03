package com.ecommerce.authorize.authserver.config;

import com.ecommerce.authorize.authserver.security.CustomAuthorizationTokenServices;
import com.ecommerce.authorize.authserver.security.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;


@Configuration
@EnableAuthorizationServer
public class Auth2Config extends AuthorizationServerConfigurerAdapter
{
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private WebResponseExceptionTranslator webResponseExceptionTranslator;

	@Bean
	public JdbcClientDetailsService jdbcClientDetailsService()
	{
		return new JdbcClientDetailsService(dataSource);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception
	{
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception
	{
		clients.withClientDetails(jdbcClientDetailsService());
	}

	@Bean
	public JdbcTokenStore jdbcTokenStore()
	{
		return new JdbcTokenStore(dataSource);
	}

	public AuthorizationServerTokenServices authorizationServerTokenServices()
	{
		CustomAuthorizationTokenServices customAuthorizationTokenServices = new CustomAuthorizationTokenServices();
		customAuthorizationTokenServices.setTokenStore(jdbcTokenStore());
		customAuthorizationTokenServices.setSupportRefreshToken(true);
		customAuthorizationTokenServices.setReuseRefreshToken(true);
		customAuthorizationTokenServices.setClientDetailsService(jdbcClientDetailsService());
		customAuthorizationTokenServices.setTokenEnhancer(accessTokenConverter());
		return customAuthorizationTokenServices;
	}

	public JwtAccessTokenConverter accessTokenConverter()
	{
		JwtAccessTokenConverter converter = new CustomTokenEnhancer();
		converter.setSigningKey("secret");
		return converter;
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
	{
		endpoints.authenticationManager(authenticationManager).tokenStore(jdbcTokenStore())
			  .tokenServices(authorizationServerTokenServices()).accessTokenConverter(accessTokenConverter())
			  .exceptionTranslator(webResponseExceptionTranslator);
	}
}
