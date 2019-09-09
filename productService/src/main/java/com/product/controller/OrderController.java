package com.product.controller;

import com.product.entity.CartEntity;
import com.product.entity.OrderEntryEntity;
import com.product.service.OrderService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/order")
public class OrderController
{
	@Autowired
	private OrderService orderService;

	@GetMapping
	public void createOrder()
	{
		CartEntity cart = new CartEntity();
		long orderId = RandomUtils.nextLong();
		cart.setId(orderId);
		cart.setTotalPrice(100.00);
		List<OrderEntryEntity> entries = new ArrayList<>();
		OrderEntryEntity entry = new OrderEntryEntity();
		entry.setId(RandomUtils.nextLong());
		entry.setOrderId(orderId);
		entry.setProductId(1L);
		entry.setQuantity(1L);
		entries.add(entry);
		cart.setEntries(entries);
		orderService.createOrder(cart);
	}
}
