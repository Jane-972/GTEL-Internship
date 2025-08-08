package org.jane.gtelinternship.product.api.dto.response;

public record ProductResponseDto(
  String sku,
  String name,
  String brand,
  PriceDto price,
  org.jane.gtelinternship.product.domain.model.ProductStock inventory
) {}