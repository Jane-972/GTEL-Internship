package org.jane.gtelinternship.product.api.dto.response;

public record PriceDto(
  String sku,
  String PriceExclVAT,
  String VAT,
  String RecycleTax,
  String Currency
) {}
