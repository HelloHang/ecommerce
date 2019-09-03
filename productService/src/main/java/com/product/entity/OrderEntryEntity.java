package com.product.entity;

public class OrderEntryEntity
{
	private Long orderId;

	private Long number;

	private Long productId;

	private Long quantity;

	public Long getOrderId()
	{
		return orderId;
	}

	public void setOrderId(Long orderId)
	{
		this.orderId = orderId;
	}

	public Long getNumber()
	{
		return number;
	}

	public void setNumber(Long number)
	{
		this.number = number;
	}

	public Long getProduct()
	{
		return productId;
	}

	public void setProduct(ProductEntity product)
	{
		this.productId = product.getId();
	}

	public Long getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Long quantity)
	{
		this.quantity = quantity;
	}
}
