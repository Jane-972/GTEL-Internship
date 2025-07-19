package org.jane.gtelinternship.stockupdater.api.dto.logicom;

public record LogicomPriceDto(
  String PriceExclVAT,
  String VAT,
  String RecycleTax,
  String Currency
) {}