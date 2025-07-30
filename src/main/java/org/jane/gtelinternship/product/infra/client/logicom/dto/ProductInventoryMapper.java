package org.jane.gtelinternship.product.infra.client.logicom.dto;

import org.jane.gtelinternship.product.domain.model.ProductInventory;
import org.jane.gtelinternship.product.domain.model.ProductStock;

import java.util.List;

public class ProductInventoryMapper {
  public static ProductInventoryResponseDto toDto(ProductInventory inventory) {
    List<ProductStockDto> productsDto = inventory.products().stream()
      .map(ProductInventoryMapper::toDto)
      .toList();

    return new ProductInventoryResponseDto(productsDto);
  }

  public static ProductStockDto toDto(ProductStock stock) {
    return new ProductStockDto(
      stock.sku(),
      stock.availableQuantity(),
      stock.totalIncomingQuantity(),
      stock.isInStock()
    );
  }
}
