package com.product.event.listener;

import com.product.event.PlaceOrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class PlaceOrderListener
{
	private static final Logger LOG = LoggerFactory.getLogger(PlaceOrderListener.class);

	@EventListener
	@Async
	public void sendOrderToMQ(PlaceOrderEvent event)
	{
		LOG.info("Send Order To MQ");
	}
}
