package org.jane.gtelinternship.product.infra.client.woo.dto;

public record WooProductDto(
  Long id,
  String sku,
  String name,
  String shortDescription,
  Boolean inStock,
  Integer stockQuantity,
  Double price,
  Double regularPrice
) {}
