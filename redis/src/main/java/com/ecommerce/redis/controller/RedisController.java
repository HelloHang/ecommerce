package com.ecommerce.redis.controller;

import com.ecommerce.redis.service.RedisService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/redis")
public class RedisController {

  @Autowired
  private RedisService redisService;

  @ApiOperation(value = "Put value to redis")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "key", value = "Key", required = true, dataType = "String")
  })
  @PutMapping
  public void putValueToRedis(@RequestParam(value = "key") String key,
      @RequestBody @ApiParam(name = "value", value = "Value", required = true) String value) {
    redisService.put(key, value);
  }

  @ApiOperation(value = "Get value to redis")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "key", value = "Key", required = true, dataType = "String")
  })
  @GetMapping
  public String getValueFromRedis(@RequestParam(value = "key") String key) {
    return redisService.get(key);
  }

  @ApiOperation(value = "Delete value to redis")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "key", value = "Key", required = true, dataType = "String")
  })
  @DeleteMapping
  public void deleteValueFormRedis(@RequestParam(value = "key") String key) {
    redisService.delete(key);
  }

}
