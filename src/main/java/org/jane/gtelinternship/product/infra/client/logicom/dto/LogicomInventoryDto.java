package org.jane.gtelinternship.product.infra.client.logicom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jane.gtelinternship.product.domain.model.ProductInventory;
import org.jane.gtelinternship.product.domain.model.ProductStock;

import java.util.List;

public record LogicomInventoryDto(
  @JsonProperty("StatusCode") int statusCode,
  @JsonProperty("Status") String status,
  @JsonProperty("Message") List<LogicomProductInventory> message
) {
  public ProductInventory toModel() {
    List<ProductStock> products = message()
      .stream()
      .map(LogicomProductInventory::toModel)
      .toList();

    return new ProductInventory(products);
  }
}
