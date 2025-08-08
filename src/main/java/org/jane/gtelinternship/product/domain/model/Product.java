package org.jane.gtelinternship.product.domain.model;

import java.util.List;

public record Product(
  String sku,
  String name,
  String brand,
  String description,
  String category,
  String priceExclVat,
  String vat,
  String recycleTax,
  String currency,
  int intelPoints,
  String warranty,
  List<String> images
) {}
