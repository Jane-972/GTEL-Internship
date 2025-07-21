package org.jane.gtelinternship.product.infra.client.logicom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jane.gtelinternship.product.domain.model.ProductStock;
import org.jane.gtelinternship.product.domain.model.PurchaseOrder;

import java.util.List;

public record LogicomProductInventory(
  @JsonProperty("SKU") String sku,
  @JsonProperty("Inventory") String inventory,
  @JsonProperty("PO") List<LogicomPurchaseOrder> po
) {
  public Integer getInventory() {
    try {
      return Integer.parseInt(inventory);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  public ProductStock toModel() {
    List<PurchaseOrder> orders;

    if (po() != null) {
      orders = po()
        .stream()
        .map(LogicomPurchaseOrder::toModel)
        .toList();
    } else {
      orders = List.of();
    }

    return new ProductStock(sku, getInventory(), orders);
  }
}
