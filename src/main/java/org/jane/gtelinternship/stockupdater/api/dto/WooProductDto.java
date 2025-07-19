package org.jane.gtelinternship.stockupdater.api.dto;


import java.util.List;

public record WooProductDto(
  Long id,
  String sku,
  String name,
  String shortDescription,
  Boolean inStock,
  Integer stockQuantity,
  Double salePrice,
  Double regularPrice,
  List<String> images,
  String attribute1Name,
  String attribute1Value,
  String attribute2Name,
  String attribute2Value,
  String attribute3Name,
  String attribute3Value
) {}
