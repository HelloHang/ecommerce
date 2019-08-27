package com.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient("redisService")
@RequestMapping("/redis")
public interface RedisClient
{
	@PostMapping
	void addValueToRedis(@RequestParam(value = "key") String key, @RequestBody String value);

	@GetMapping
	String getValueFromRedis(@RequestParam(value = "key") String key);

	@DeleteMapping
	void deleteValueFormRedis(@RequestParam(value = "key") String key);
}
