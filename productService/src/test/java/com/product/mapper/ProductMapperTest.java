package com.product.mapper;

import static org.junit.Assert.*;

import com.product.entity.ProductEntity;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductMapperTest {

  @Autowired
  private ProductMapper productMapper;

  @Test
  public void insert() {
    productMapper.insert(new ProductEntity("product1", "test product1","normal"));
    productMapper.insert(new ProductEntity("product2", "test product2","normal"));
    assertEquals(2, productMapper.getAll().size());
  }

  @Test
  public void getOne() {
    final ProductEntity productEntity = productMapper.getOne(1L);
    assertEquals("product1",productEntity.getName());
  }

  @Test
  public void update() {
    final ProductEntity one = productMapper.getOne(1L);
    one.setName("update name");
    productMapper.update(one);
    assertEquals("update name", productMapper.getOne(1L).getName());
  }

  @Test
  public void delete() {
    productMapper.delete(1L);
    assertEquals(1, productMapper.getAll().size());
  }
}