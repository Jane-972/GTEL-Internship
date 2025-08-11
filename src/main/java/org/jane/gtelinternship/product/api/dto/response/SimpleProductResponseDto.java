package org.jane.gtelinternship.product.api.dto.response;

import org.jane.gtelinternship.product.domain.model.Product;

public record SimpleProductResponseDto(
  String sku,
  String name,
  String brand
) {
  public static SimpleProductResponseDto fromModel(Product model) {
    return new SimpleProductResponseDto(
      model.sku(),
      model.name(),
      model.brand()
    );
  }
}