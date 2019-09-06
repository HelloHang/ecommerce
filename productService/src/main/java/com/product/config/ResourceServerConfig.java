package com.product.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.servlet.http.HttpServletResponse;


@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter
{
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Bean
	public RedisTokenStore tokenStore()
	{
		return new RedisTokenStore(redisConnectionFactory);
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception
	{
		super.configure(resources);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception
	{
		http.csrf().disable().exceptionHandling().authenticationEntryPoint(
			  (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
			  .authorizeRequests().antMatchers(HttpMethod.DELETE).hasRole("ADMIN").anyRequest().authenticated().and().httpBasic();
	}
}
