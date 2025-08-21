package org.jane.gtelinternship.product.infra.client.logicom.dto;


import java.util.List;

public record ProductPriceDto(
  String SKU,
  String PriceExclVAT,
  String VAT,
  String RecycleTax,
  String Currency,
  List<VolumePriceDto> VolumePrice
) {}
