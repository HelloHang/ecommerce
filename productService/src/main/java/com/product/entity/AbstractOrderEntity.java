package com.product.entity;

import java.util.List;


public abstract class AbstractOrderEntity
{
	private Long id;

	private List<OrderEntity> entities;

	private Double totalPrice;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public List<OrderEntity> getEntities()
	{
		return entities;
	}

	public void setEntities(List<OrderEntity> entities)
	{
		this.entities = entities;
	}

	public Double getTotalPrice()
	{
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice)
	{
		this.totalPrice = totalPrice;
	}
}
