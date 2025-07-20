package org.jane.gtelinternship.product.infra.client.logicom.dto;


import java.util.List;

public record LogicomInventoryDto(
  int statusCode,
  String status,
  List<LogicomProductInventory> message
) {
}