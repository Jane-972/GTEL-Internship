package org.jane.gtelinternship.product.infra.client.logicom.mapper;

import org.jane.gtelinternship.product.api.dto.response.ProductDto;
import org.jane.gtelinternship.product.domain.model.Product;

import java.util.List;

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
      price != null ? price.PriceExclVAT() : null,
      price != null ? price.VAT() : null,
      price != null ? price.RecycleTax() : null,
      price != null ? price.Currency() : null,
      dto.IntelPoints(),
      dto.Warranty(),
      dto.Images()
    );
  }
}
