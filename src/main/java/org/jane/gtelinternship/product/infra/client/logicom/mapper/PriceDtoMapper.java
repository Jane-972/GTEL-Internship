package org.jane.gtelinternship.product.infra.client.logicom.mapper;

import jakarta.annotation.Nullable;
import org.jane.gtelinternship.product.domain.model.ProductPrice;
import org.jane.gtelinternship.product.infra.client.logicom.dto.ProductPriceDto;

import java.util.Currency;

import static org.jane.gtelinternship.product.infra.client.logicom.mapper.LogicomPriceUtil.parsePrice;

public class PriceDtoMapper {
  @Nullable
  public static ProductPrice from(ProductPriceDto dto) {
    Double amountExclVat = parsePrice(dto.PriceExclVAT());
    Double vat = parsePrice(dto.VAT());

    if (amountExclVat == null || vat == null) {
      return null;
    } else {
      return new ProductPrice(
        amountExclVat + vat,
        Currency.getInstance(dto.Currency())
      );
    }
  }
}
