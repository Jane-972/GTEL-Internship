package org.jane.gtelinternship.product.infra.client.logicom.dto;

public record LogicomPriceDto(
  String sku,
  String PriceExclVAT,
  String VAT,
  String Currency
) {
}