package com.product.service.impl;

import com.product.entity.CartEntity;
import com.product.entity.OrderEntity;
import com.product.mapper.OrderEntityMapper;
import com.product.mapper.OrderMapper;
import com.product.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DefaultOrderService implements OrderService
{
	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderEntityMapper orderEntityMapper;

	@Override
	public OrderEntity createOrder(CartEntity cart)
	{
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setId(cart.getId());
		orderEntity.setTotalPrice(cart.getTotalPrice());
		orderEntity.setEntries(cart.getEntries());
		orderMapper.insert(orderEntity);
		orderEntityMapper.batchInsert(orderEntity.getEntries());
		return orderEntity;
	}


}
