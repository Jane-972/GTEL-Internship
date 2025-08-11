package org.jane.gtelinternship.product.domain.model;

import java.util.Currency;

public record ProductPrice(
  double amountExclVat,
  double vat,
  Currency currency

) {
  public double fullPrice() {
    return  amountExclVat + vat;
  }
}
