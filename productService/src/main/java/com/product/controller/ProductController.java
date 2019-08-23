package com.product.controller;

import com.product.entity.ProductEntity;
import com.product.mapper.ProductMapper;
import com.product.service.ProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author: Daniels Gao
 * @date: 2019/8/13 16:14
 */
@RestController
@RequestMapping("/product")
public class ProductController
{
	@Autowired
	private ProductService productService;

	@ApiOperation(value = "Get Product By Id", notes = "Id must be number.")
	@ApiImplicitParam(name = "id", value = "Product Id", required = true, dataType = "Long")
	@GetMapping("/{id}")
	public ProductEntity getProduct(@PathVariable("id") final Long id)
	{
		return productService.getProductById(id);
	}

	@ApiOperation(value = "Add Product")
	@PostMapping
	public void addProduct(@RequestBody @ApiParam(name = "productEntity", value = "Product Entity", required = true)
	final ProductEntity productEntity)
	{
		productService.addProduct(productEntity);
	}

	@ApiOperation(value = "Delete Product By Id", notes = "Id must be number.")
	@ApiImplicitParam(name = "id", value = "Product Id", required = true, dataType = "Long")
	@DeleteMapping("/{id}")
	public void deleteProduct(@PathVariable("id") final Long id)
	{
		productService.deleteProduct(id);
	}

	@ApiOperation(value = "Update Product")
	@PutMapping
	public void updateProduct(@RequestBody @ApiParam(name = "productEntity", value = "Product Entity", required = true)
	final ProductEntity productEntity)
	{
		productService.updateProduct(productEntity);
	}

}
