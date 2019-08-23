package com.product.service;

import com.product.entity.ProductEntity;


public interface ProductService
{
	ProductEntity getProductById(Long id);

	void addProduct(ProductEntity productEntity);

	void updateProduct(ProductEntity productEntity);

	void deleteProduct(Long id);

}
