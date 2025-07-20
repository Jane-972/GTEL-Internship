package org.jane.gtelinternship.product.infra.client.logicom.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

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
}