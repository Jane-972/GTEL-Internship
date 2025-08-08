package org.jane.gtelinternship.product.api.dto.response;


import java.util.List;

public record ProductPriceDto(
  String SKU,
  String PriceExclVAT,
  String VAT,
  String RecycleTax,
  String Currency,
  List<VolumePriceDto> VolumePrice
) {}
