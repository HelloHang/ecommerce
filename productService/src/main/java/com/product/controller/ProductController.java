package com.product.controller;

import com.product.entity.ProductEntity;
import com.product.mapper.ProductMapper;
import com.product.service.ProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Daniels Gao
 * @date: 2019/8/13 16:14
 */
@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductService productService;

  @ApiOperation(value = "Get Product By Id", notes = "Id must be number.")
  @ApiImplicitParam(name = "id", value = "Product Id", required = true, dataType = "Long")
  @GetMapping("/{id}")
  public ProductEntity getProduct(@PathVariable("id") final Long id) {
     return productService.getProductById(id);
  }

}
