package org.jane.gtelinternship.product.domain.model;

import java.util.List;

public record ProductInventory(
  List<ProductStock> products
) {
  public static ProductInventory empty() {
    return new ProductInventory(List.of());
  }

  public boolean isEmpty() {
    return products.isEmpty();
  }

  public ProductStock findBySku(String sku) {
    return products.stream()
      .filter(p -> p.sku().equals(sku))
      .findFirst()
      .orElse(null);
  }
}
