package org.jane.gtelinternship.product.api.dto.response;

import org.jane.gtelinternship.product.domain.model.FullProduct;
import org.jane.gtelinternship.product.domain.model.LogicomProduct;

public record ProductResponseDto(
  String sku,
  String name,
  String brand,
  PriceDto price,
  int availableStock
) {
  public static ProductResponseDto fromModel(FullProduct<LogicomProduct> model) {
    return new ProductResponseDto(
      model.product().getSku(),
      model.product().getName(),
      model.product().getBrand(),
      PriceDto.fromModel(model.price()),
      model.availableStock()
    );
  }
}