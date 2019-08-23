package com.product.ehcache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.product.entity.ProductEntity;

import java.util.Map;

import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;


public class CustomCacheLoaderWriter implements CacheLoaderWriter
{

	public static final String EMPTY_OBJECT = "{}";
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
		String value = restTemplate.getForObject("http://localhost:8002/redis?key={?}", String.class, key);
		if (value != null && !EMPTY_OBJECT.equals(value))
		{
			LOG.info("Get data form custom cache.");
			return objectMapper.readValue(value, ProductEntity.class);
		}
		return null;
	}

	@Override
	public void write(Object key, Object value) throws Exception
	{
		if (value != null)
		{
			objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			LOG.info("Writer data to custom cache.");
			final String json = objectMapper.writeValueAsString(value);
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);
			restTemplate.postForLocation("http://localhost:8002/redis?key={?}", httpEntity, key);
			LOG.info("Writer data to custom cache successfully.");
		}
	}

	@Override
	public void delete(Object key) throws Exception
	{
		restTemplate.delete("http://localhost:8002/redis?key={?}", key);
		LOG.info("delete data form redis successful.");
	}
}
