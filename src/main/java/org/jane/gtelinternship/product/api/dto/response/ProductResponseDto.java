package org.jane.gtelinternship.product.api.dto.response;

import org.jane.gtelinternship.product.domain.model.FullProduct;
import org.jane.gtelinternship.product.domain.model.LogicomProduct;

import java.util.List;

public record ProductResponseDto(
  String sku,
  String name,
  String brand,
  List<String> image,
  PriceDto price,
  int availableStock
) {
  public static ProductResponseDto fromModel(FullProduct<LogicomProduct> model) {
    return new ProductResponseDto(
      model.product().getSku(),
      model.product().getName(),
      model.product().getBrand(),
      model.product().getImage(), // Mapping images that were forgotten
      PriceDto.fromModel(model.price()),
      model.availableStock()
    );
  }
}
