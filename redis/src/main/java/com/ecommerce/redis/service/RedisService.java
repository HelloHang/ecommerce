package com.ecommerce.redis.service;

public interface RedisService
{
	void put(String key, String value);

	String get(String key);

	void delete(String key);
}
