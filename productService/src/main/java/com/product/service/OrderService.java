package com.product.service;

import com.product.entity.CartEntity;
import com.product.entity.OrderEntity;


public interface OrderService
{
	OrderEntity createOrder(CartEntity cart);
}
