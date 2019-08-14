package com.product.entity;

/**
 * @author: Daniels Gao
 * @date: 2019/8/13 15:03
 */
public class ProductEntity {
  private Long id;
  private String name;
  private String description;
  private String productType;

  public ProductEntity(String name, String description, String productType) {
    this.name = name;
    this.description = description;
    this.productType = productType;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
