package org.jane.gtelinternship.product.infra.client.logicom.mapper;

import org.jane.gtelinternship.product.domain.model.FullProduct;
import org.jane.gtelinternship.product.domain.model.LogicomProduct;
import org.jane.gtelinternship.product.domain.model.ProductPrice;
import org.jane.gtelinternship.product.infra.client.logicom.dto.ProductDto;

import java.util.Currency;
import java.util.List;
import java.util.stream.Stream;

import static org.jane.gtelinternship.product.infra.client.logicom.mapper.LogicomPriceUtil.parsePrice;

public class ProductDtoMapper {

  public static Stream<FullProduct<LogicomProduct>> mapToDomain(List<ProductDto> dtos) {
    if (dtos == null || dtos.isEmpty()) {
      return Stream.empty();
    }

    return dtos.stream()
      .map(ProductDtoMapper::mapToModel);
  }

  private static FullProduct<LogicomProduct> mapToModel(ProductDto dto) {
    var price = dto.Price();

    return new FullProduct<>(new LogicomProduct(
      dto.SKU(),
      dto.Name(),
      dto.Manufacturer(),
      dto.Description(),
      dto.Category(),
      dto.Images()
    ),
      new ProductPrice(parsePrice(price.PriceExclVAT() + price.VAT()), Currency.getInstance(price.Currency())),
      dto.Inventory().Quantity()
    );
  }
}
