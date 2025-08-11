package org.jane.gtelinternship.product.api.dto.response;

import org.jane.gtelinternship.product.domain.model.FullProduct;

public record ProductResponseDto(
  String sku,
  String name,
  String brand,
  PriceDto price,
  int availableStock
) {
  public static ProductResponseDto fromModel(FullProduct model) {
    return new ProductResponseDto(
      model.product().sku(),
      model.product().name(),
      model.product().brand(),
      PriceDto.fromModel(model.price()),
      model.availableStock()
    );
  }
}