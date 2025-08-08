package org.jane.gtelinternship.product.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public record InventoryDto(
  int Quantity,
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  List<ProductPODto> PO
) {}
