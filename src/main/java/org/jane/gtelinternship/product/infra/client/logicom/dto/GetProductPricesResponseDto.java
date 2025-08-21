package org.jane.gtelinternship.product.infra.client.logicom.dto;

import java.util.List;

public record GetProductPricesResponseDto(
  int StatusCode,
  String Status,
  List<ProductPriceDto> Message
) {}
