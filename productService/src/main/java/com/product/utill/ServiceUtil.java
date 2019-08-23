package com.product.utill;

import com.google.common.base.Preconditions;


public class ServiceUtil
{
	public static void validateParameterNotNull(Object parameter, String nullMessage)
	{
		Preconditions.checkArgument(parameter != null, nullMessage);
	}
}
