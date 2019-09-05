package com.ecommerce.authorize.authserver.config;

import com.ecommerce.authorize.authserver.handler.CustomeLogoutHandler;
import com.ecommerce.authorize.authserver.provider.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		//TODO : LogoutHandler需要重写
		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().requestMatchers()
			  .antMatchers("/**").and().authorizeRequests().antMatchers("/**").permitAll().anyRequest().authenticated().and()
			  .logout().logoutUrl("/logout").clearAuthentication(true)
			  .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()).addLogoutHandler(customLogoutHandler());
	}

	@Bean
	public LogoutHandler customLogoutHandler()
	{
		return new CustomeLogoutHandler();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		//方式1：自定义认证流程
//		auth.authenticationProvider(customAuthenticationProvider);

//		方式2：
		auth.inMemoryAuthentication()
			  .withUser("john").password("123").roles("USER");
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
		return super.authenticationManagerBean();
	}

	@Override
	@Bean
	public UserDetailsService userDetailsServiceBean() throws Exception
	{
		return super.userDetailsServiceBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return NoOpPasswordEncoder.getInstance();
	}
}
