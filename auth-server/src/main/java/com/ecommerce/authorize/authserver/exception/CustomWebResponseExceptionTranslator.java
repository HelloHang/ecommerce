package com.ecommerce.authorize.authserver.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;


public class CustomWebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator
{
	@Override
	public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception
	{
		return super.translate(e);
	}
}
