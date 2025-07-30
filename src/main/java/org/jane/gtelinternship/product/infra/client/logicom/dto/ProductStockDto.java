package org.jane.gtelinternship.product.infra.client.logicom.dto;

public record ProductStockDto(
  String sku,
  int availableQuantity,
  int totalIncomingQuantity,
  boolean inStock
) {}

