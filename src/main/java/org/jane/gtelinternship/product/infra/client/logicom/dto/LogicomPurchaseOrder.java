package org.jane.gtelinternship.product.infra.client.logicom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jane.gtelinternship.product.domain.model.PurchaseOrder;

public record LogicomPurchaseOrder(
  @JsonProperty("Quantity") String quantity,
  @JsonProperty("PODeliveryDate") String poDeliveryDate
) {
  public Integer getQuantity() {
    try {
      return Integer.parseInt(quantity);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  public PurchaseOrder toModel() {
    return new PurchaseOrder(
      "PO-" + hashCode(),
      getQuantity(),
      poDeliveryDate()
    );
  }
}
