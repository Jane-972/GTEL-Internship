package org.jane.gtelinternship.product.domain.model;

import java.util.Currency;

public record ProductPrice(
  double amount,
  Currency currency

) {
}
