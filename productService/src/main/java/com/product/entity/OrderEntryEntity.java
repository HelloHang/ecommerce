package com.product.entity;

public class OrderEntryEntity
{
	private Long orderId;

	private Long id;

	private Long productId;

	private ProductEntity productEntity;

	private Long quantity;

	public Long getOrderId()
	{
		return orderId;
	}

	public void setOrderId(Long orderId)
	{
		this.orderId = orderId;
	}

	public Long getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Long quantity)
	{
		this.quantity = quantity;
	}

	public ProductEntity getProductEntity()
	{
		return productEntity;
	}

	public void setProductEntity(ProductEntity productEntity)
	{
		this.productEntity = productEntity;
	}

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}
}
