package org.jane.gtelinternship.product.api.dto.response;

import org.jane.gtelinternship.product.domain.model.ProductInventory;

import java.util.List;

public record ProductInventoryResponseDto(
  List<ProductStockDto> products
) {
  public static ProductInventoryResponseDto from(ProductInventory inventory) {
    List<ProductStockDto> dtoList = inventory.products().stream()
      .map(ProductStockDto::from)
      .toList();

    return new ProductInventoryResponseDto(dtoList);
  }
}
