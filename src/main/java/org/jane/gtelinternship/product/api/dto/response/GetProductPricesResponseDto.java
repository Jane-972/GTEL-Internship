package org.jane.gtelinternship.product.api.dto.response;

import java.util.List;

public record GetProductPricesResponseDto(
  int StatusCode,
  String Status,
  List<ProductPriceDto> Message
) {}
