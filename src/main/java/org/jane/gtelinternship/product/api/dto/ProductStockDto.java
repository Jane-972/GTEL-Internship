package org.jane.gtelinternship.product.api.dto;

import org.jane.gtelinternship.product.domain.model.ProductStock;

public record ProductStockDto(
  String sku,
  int availableQuantity,
  int totalIncomingQuantity,
  boolean inStock
) {
  public static ProductStockDto from(ProductStock stock) {
    return new ProductStockDto(
      stock.sku(),
      stock.availableQuantity(),
      stock.totalIncomingQuantity(),
      stock.isInStock()
    );
  }
}
