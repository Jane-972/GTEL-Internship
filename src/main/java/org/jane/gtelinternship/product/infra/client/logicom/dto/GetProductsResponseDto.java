package org.jane.gtelinternship.product.infra.client.logicom.dto;

import java.util.List;

public record GetProductsResponseDto(
  int StatusCode,
  String Status,
  List<ProductDto> Message
) {}
