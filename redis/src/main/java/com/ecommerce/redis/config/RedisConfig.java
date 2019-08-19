package com.ecommerce.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;


/**
 * @author: Daniels Gao
 * @date: 2019/8/13 17:43
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

  @Bean
  @Override
  public KeyGenerator keyGenerator() {
    return (o, method, objects) -> {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(o.getClass().getName());
      stringBuilder.append(method.getName());
      for (Object object : objects) {
        stringBuilder.append(object.toString());
      }
      return stringBuilder.toString();
    };
  }

  @Bean
  public CacheManager cacheManager(LettuceConnectionFactory factory) {
    final RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(factory);
    final RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
        .defaultCacheConfig();
    return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
  }

  @Bean
  public RedisTemplate<String, String> redisTemplate(LettuceConnectionFactory factory) {
    final StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
    stringRedisTemplate.setConnectionFactory(factory);
    Jackson2JsonRedisSerializer jsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
    objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
    jsonRedisSerializer.setObjectMapper(objectMapper);
    stringRedisTemplate.setValueSerializer(jsonRedisSerializer);
    stringRedisTemplate.afterPropertiesSet();
    return stringRedisTemplate;
  }
}
