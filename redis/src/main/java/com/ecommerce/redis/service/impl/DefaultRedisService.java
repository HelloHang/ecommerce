package com.ecommerce.redis.service.impl;

import com.ecommerce.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;


@Service
public class DefaultRedisService implements RedisService
{
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public void put(String key, String value)
	{
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		opsForValue.set(key,value);
	}

	@Override
	public String get(String key)
	{
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		return opsForValue.get(key);
	}

	@Override
	public void delete(String key)
	{
		redisTemplate.delete(key);
	}
}
