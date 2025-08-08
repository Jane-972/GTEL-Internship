package org.jane.gtelinternship.product.api.dto.response;

import java.util.List;

public record GetProductsResponseDto(
  int StatusCode,
  String Status,
  List<ProductDto> Message
) {}
