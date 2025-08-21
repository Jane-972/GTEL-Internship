package org.jane.gtelinternship.product.api.dto.response;

public record ProductComparisonDto(
  String name,
  String sku,
  Double wooPrice,
  Double logicomPrice,
  Double difference,
  String status
) {}
