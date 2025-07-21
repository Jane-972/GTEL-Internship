package org.jane.gtelinternship.product.infra.client.logicom.domain;

public record PurchaseOrder(
  String orderNumber,
  int quantity,
  String expectedDate
) {

}
