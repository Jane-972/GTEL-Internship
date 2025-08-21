package org.jane.gtelinternship.product.api.dto.response;

import lombok.NonNull;
import org.jane.gtelinternship.product.domain.model.ProductPrice;

public record PriceDto(
  double amount,
  String currency
) {
  public static PriceDto fromModel(@NonNull ProductPrice price) {
    return new PriceDto(
      price.amount(),
      price.currency().toString()
    );
  }
}
