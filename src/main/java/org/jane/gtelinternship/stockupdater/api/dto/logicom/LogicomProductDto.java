package org.jane.gtelinternship.stockupdater.api.dto.logicom;

import java.util.List;

public record LogicomProductDto(
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
  List<LogicomSpecificationDto> Specifications,
  LogicomInventoryDto Inventory,
  List<String> Images,
  List<Object> Variants
) {}
