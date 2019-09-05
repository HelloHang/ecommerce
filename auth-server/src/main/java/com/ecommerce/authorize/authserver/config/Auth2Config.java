package com.ecommerce.authorize.authserver.config;

import com.ecommerce.authorize.authserver.security.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

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

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public JdbcClientDetailsService jdbcClientDetailsService()
	{
		return new JdbcClientDetailsService(dataSource);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception
	{
		security.allowFormAuthenticationForClients();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception
	{
		//方式1.使用JDBC从数据库中查找client信息
		clients.withClientDetails(jdbcClientDetailsService());

		//		方式2. 同时向数据库中写入client信息
//		clients.jdbc(dataSource).withClient("client1").authorizedGrantTypes("client_credentials", "refresh_token").scopes("read")
//			  .secret("secret1").and().withClient("clientIdPassword").secret("secret")
//			  .authorizedGrantTypes("password", "authorization_code", "refresh_token").scopes("read");

		//方式3：使用内存存储
		//		clients.inMemory().withClient();
	}

	//	@Bean
	//	public JdbcTokenStore jdbcTokenStore()
	//	{
	//		return new JdbcTokenStore(dataSource);
	//	}

	//	public AuthorizationServerTokenServices authorizationServerTokenServices()
	//	{
	//		CustomAuthorizationTokenServices customAuthorizationTokenServices = new CustomAuthorizationTokenServices();
	//		customAuthorizationTokenServices.setTokenStore(jdbcTokenStore());
	//		customAuthorizationTokenServices.setSupportRefreshToken(true);
	//		customAuthorizationTokenServices.setReuseRefreshToken(true);
	//		customAuthorizationTokenServices.setClientDetailsService(jdbcClientDetailsService());
	//		customAuthorizationTokenServices.setTokenEnhancer(accessTokenConverter());
	//		return customAuthorizationTokenServices;
	//	}

	public JwtAccessTokenConverter accessTokenConverter()
	{
		JwtAccessTokenConverter converter = new CustomTokenEnhancer();
		converter.setSigningKey("secret");
		return converter;
	}

	@Bean
	public RedisTokenStore redisTokenStore()
	{
		return new RedisTokenStore(redisConnectionFactory);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
	{
		//		endpoints.authenticationManager(authenticationManager).tokenStore(jdbcTokenStore())
		//			  .tokenServices(authorizationServerTokenServices()).accessTokenConverter(accessTokenConverter())
		//			  .exceptionTranslator(webResponseExceptionTranslator);
		//方式1： JDBC存储Token
		//		endpoints.tokenStore(jdbcTokenStore()).authenticationManager(authenticationManager);

		//方式2： Redis存储Token
		endpoints.tokenStore(redisTokenStore()).authenticationManager(authenticationManager)
			  .userDetailsService(userDetailsService);
	}
}
