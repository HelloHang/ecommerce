package com.product.service.impl;

import com.product.entity.ProductEntity;
import com.product.mapper.ProductMapper;
import com.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class DefaultProductService implements ProductService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultProductService.class);

	@Autowired
	private ProductMapper productMapper;

	@Override
	@Cacheable(value = "productCache", key = "#id")
	public ProductEntity getProductById(Long id)
	{
		LOG.info("Get product from db.");
		return productMapper.getOne(id);
	}
}
