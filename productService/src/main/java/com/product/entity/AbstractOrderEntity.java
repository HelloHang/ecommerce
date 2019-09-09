package com.product.entity;

import java.util.List;


public abstract class AbstractOrderEntity
{
	private Long id;

	private List<OrderEntryEntity> entries;

	private Double totalPrice;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Double getTotalPrice()
	{
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice)
	{
		this.totalPrice = totalPrice;
	}

	public List<OrderEntryEntity> getEntries()
	{
		return entries;
	}

	public void setEntries(List<OrderEntryEntity> entries)
	{
		this.entries = entries;
	}
}
