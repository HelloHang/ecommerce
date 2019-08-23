package com.product.service.impl;

import com.product.entity.ProductEntity;
import com.product.mapper.ProductMapper;
import com.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.product.utill.ServiceUtil.validateParameterNotNull;


@Service
public class DefaultProductService implements ProductService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultProductService.class);

	@Autowired
	private ProductMapper productMapper;

	@Override
	@Cacheable(value = "productCache", key = "#id", unless = "#result == null")
	public ProductEntity getProductById(Long id)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("Get product[id=%s] from db.", id));
		}
		return productMapper.getOne(id);
	}

	@Override
	public void addProduct(ProductEntity productEntity)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("Add Product[%s] to DB", productEntity.toString()));
		}
		productMapper.insert(productEntity);
	}

	@Override
	@CacheEvict(value = "productCache", key = "#productEntity.getId()")
	public void updateProduct(ProductEntity productEntity)
	{
		validateParameterNotNull(productEntity.getId(), "Product Id can't be empty");
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("Update Product[%s] in DB.", productEntity.toString()));
		}
		productMapper.update(productEntity);
	}

	@Override
	@CacheEvict(value = "productCache", key = "#id")
	public void deleteProduct(Long id)
	{
		validateParameterNotNull(id, "Product Id can't be empty");
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("Delete product[id=%s] from DB", id));
		}
		productMapper.delete(id);
	}


}
