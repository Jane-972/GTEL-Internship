package org.jane.gtelinternship.product.domain.model;

public record PurchaseOrder(
  String orderNumber,
  int quantity,
  String expectedDate
) {

}
