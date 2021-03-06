package com.ecommerce.authorize.authserver.config;

import com.ecommerce.authorize.authserver.exception.CustomWebResponseExceptionTranslator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;


@Configuration
public class ServiceConfig
{
	@Bean
	public WebResponseExceptionTranslator webResponseExceptionTranslator()
	{
		return new CustomWebResponseExceptionTranslator();
	}
}
