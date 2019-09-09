package com.product.mapper;

import com.product.entity.OrderEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface OrderMapper
{
	@Insert("Insert into orders(order_id, total_price) values(#{id},#{totalPrice})")
	void insert(OrderEntity orderEntity);
}
