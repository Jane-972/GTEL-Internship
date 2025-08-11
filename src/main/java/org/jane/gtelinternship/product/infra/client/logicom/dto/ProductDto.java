package org.jane.gtelinternship.product.infra.client.logicom.dto;

import java.util.List;

public record ProductDto(
  String SKU,
  String Name,
  String Description,
  String Manufacturer,
  String Category,
  String IsESD,
  String IsEUItem,
  String Barcode,
  String HasVariantProducts,
  LogicomPriceDto Price,
  int IntelPoints,
  String Warranty,
  List<SpecificationDto> Specifications,
  InventoryDto Inventory,
  List<String> Images,
  List<Object> Variants
) {}
