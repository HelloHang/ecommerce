package com.product.event;

import org.springframework.context.ApplicationEvent;


public class PlaceOrderEvent extends ApplicationEvent
{
	public PlaceOrderEvent(Object source)
	{
		super(source);
	}
}
