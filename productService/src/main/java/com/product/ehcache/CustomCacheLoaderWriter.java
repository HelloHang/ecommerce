package com.product.ehcache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.product.client.RedisClient;
import com.product.entity.ProductEntity;

import java.util.Map;

import com.product.utill.SpringContextUtil;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;


public class CustomCacheLoaderWriter implements CacheLoaderWriter
{
	private static final Logger LOG = LoggerFactory.getLogger(CustomCacheLoaderWriter.class);
	public static final String EMPTY_OBJECT = "{}";

	private RedisClient redisClient;

	private ObjectMapper objectMapper;


	public CustomCacheLoaderWriter()
	{
		objectMapper = new ObjectMapper();
		redisClient = SpringContextUtil.getBean(RedisClient.class);
	}

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
//		String value = restTemplate.getForObject("http://localhost:8002/redis?key={?}", String.class, key);
		String value = redisClient.getValueFromRedis(String.valueOf(key));
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
//			restTemplate.postForLocation("http://localhost:8002/redis?key={?}", httpEntity, key);
			redisClient.addValueToRedis(String.valueOf(key), json);
			LOG.info("Writer data to custom cache successfully.");
		}
	}

	@Override
	public void delete(Object key) throws Exception
	{
//		restTemplate.delete("http://localhost:8002/redis?key={?}", key);
		redisClient.deleteValueFormRedis(String.valueOf(key));
		LOG.info("delete data form redis successful.");
	}
}
