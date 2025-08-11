package org.jane.gtelinternship.product.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import org.jane.gtelinternship.product.domain.model.ProductPrice;

public record PriceDto(
  @JsonProperty("PriceExclVAT")
  double priceExclVAT,
  @JsonProperty("VAT")
  double vat,
  @JsonProperty("Currency")
  String currency
) {
  public static PriceDto fromModel(@NonNull ProductPrice price) {
    return new PriceDto(
      price.amountExclVat(),
      price.vat(),
      price.currency().toString()
    );
  }
}
