package org.jane.gtelinternship.product.infra.client.logicom.mapper;

import org.jane.gtelinternship.product.api.dto.response.PriceDto;
import org.jane.gtelinternship.product.api.dto.response.ProductPriceDto;

public class PriceDtoMapper {
  public static PriceDto from(ProductPriceDto dto) {
    return new PriceDto(
      dto.SKU(),
      dto.PriceExclVAT(),
      dto.VAT(),
      dto.RecycleTax(),
      dto.Currency()
    );
  }
}
