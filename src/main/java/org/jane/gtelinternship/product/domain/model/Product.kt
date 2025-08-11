package org.jane.gtelinternship.product.domain.model;

import jakarta.annotation.Nullable;

import java.util.Currency;
import java.util.List;

public record Product(
  String sku,
  String name,
  String brand,
  String description,
  String category,
  @Nullable
  Double priceExclVat,
  @Nullable
  Double vat,
  Currency currency,
  List<String> images
) {
  Double getTotalPrice() {
    if (priceExclVat == null || vat == null) {
      return null;
    }
    return priceExclVat + vat;
  }
}
