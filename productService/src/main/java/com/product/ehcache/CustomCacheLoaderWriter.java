package com.product.ehcache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.entity.ProductEntity;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


public class CustomCacheLoaderWriter implements CacheLoaderWriter
{
	private RestTemplate restTemplate = new RestTemplate();

	private ObjectMapper objectMapper = new ObjectMapper();

	private static final Logger LOG = LoggerFactory.getLogger(CustomCacheLoaderWriter.class);

	@Override
	public Map loadAll(Iterable keys) throws BulkCacheLoadingException, Exception
	{
		return null;
	}

	@Override
	public void writeAll(Iterable iterable) throws BulkCacheWritingException, Exception
	{

	}

	@Override
	public void deleteAll(Iterable keys) throws BulkCacheWritingException, Exception
	{

	}

	@Override
	public Object load(Object key) throws Exception
	{
		LOG.info("Get data form custom cache.");
		String value = restTemplate.getForObject("http://local:host:8002/redis?Key={?}", String.class, key);
		if (value != null)
		{
			return objectMapper.readValue(value, ProductEntity.class);
		}
		return null;
	}

	@Override
	public void write(Object key, Object value) throws Exception
	{
		LOG.info("Writer data to custom cache.");
		restTemplate.put("http://local:host:8002/redis?Key={?}&&value={?}", key, value);
		LOG.info("Writer data to custom cache successfully.");
	}

	@Override
	public void delete(Object key) throws Exception
	{
		restTemplate.delete("http://local:host:8002/redis?Key={?}", key);
		LOG.info("delete data form redis successful.");
	}
}
