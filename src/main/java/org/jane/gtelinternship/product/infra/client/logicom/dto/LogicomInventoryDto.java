package org.jane.gtelinternship.product.infra.client.logicom.dto;

import org.jane.gtelinternship.product.infra.client.logicom.domain.ProductInventory;
import org.jane.gtelinternship.product.infra.client.logicom.domain.ProductStock;

import java.util.List;

public record LogicomInventoryDto(
  int statusCode,
  String status,
  List<LogicomProductInventory> message
) {
  public ProductInventory toModel() {
    List<ProductStock> products = message()
      .stream()
      .map(LogicomProductInventory::toModel)
      .toList();

    return new ProductInventory(products);
  }
}
