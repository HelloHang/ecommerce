package com.product.mapper;

import com.product.entity.ProductEntity;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author: Daniels Gao
 * @date: 2019/8/13 15:06
 */
@Mapper
public interface ProductMapper {

  @Select("Select * from products")
  @Results({@Result(property = "productType", column = "product_type")})
  List<ProductEntity> getAll();

  @Select("Select * from products where id=#{id}")
  @Results({@Result(property = "productType", column = "product_type")})
  ProductEntity getOne(Long id);

  @Insert("Insert into products(id,name,description,product_type) values(#{id},#{name},#{description},#{productType})")
  void insert(ProductEntity productEntity);

  @Update("Update products set name=#{name}, description=#{description}, product_type=#{productType} where id=#{id}")
  void update(ProductEntity productEntity);

  @Delete("Delete from products where id=#{id}")
  void delete(Long id);
}
