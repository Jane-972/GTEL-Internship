package org.jane.gtelinternship.product.infra.client.logicom.dto;

import java.util.List;

public record ProductInventoryResponseDto(
  List<ProductStockDto> products
) {}
