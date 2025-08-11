package org.jane.gtelinternship.product.infra.client.logicom.mapper;

import org.jane.gtelinternship.product.domain.model.Product;
import org.jane.gtelinternship.product.infra.client.logicom.dto.ProductDto;

import java.util.Currency;
import java.util.List;

import static org.jane.gtelinternship.product.infra.client.logicom.mapper.LogicomPriceUtil.parsePrice;

public class ProductDtoMapper {

  public static List<Product> mapToDomain(List<ProductDto> dtos) {
    if (dtos == null || dtos.isEmpty()) {
      return List.of();
    }

    return dtos.stream()
      .map(ProductDtoMapper::mapToModel)
      .toList();
  }

  private static Product mapToModel(ProductDto dto) {
    var price = dto.Price();

    return new Product(
      dto.SKU(),
      dto.Name(),
      dto.Manufacturer(),
      dto.Description(),
      dto.Category(),
      price != null ? parsePrice(price.PriceExclVAT()) : null,
      price != null ? parsePrice(price.VAT()) : null,
      price != null ? Currency.getInstance(price.Currency()) : null,
      dto.Images()
    );
  }
}
