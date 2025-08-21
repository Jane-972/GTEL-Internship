package org.jane.gtelinternship.product.domain.model;

import java.util.List;

// TODO: Is it used
public record ProductStock(
  String sku,
  int availableQuantity,
  List<PurchaseOrder> incomingOrders
) {
  public boolean isInStock() {
    return availableQuantity > 0;
  }

  public int totalIncomingQuantity() {
    return incomingOrders.stream()
      .mapToInt(PurchaseOrder::quantity)
      .sum();
  }
}
