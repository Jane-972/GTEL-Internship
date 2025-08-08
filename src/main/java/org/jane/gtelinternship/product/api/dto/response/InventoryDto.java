package org.jane.gtelinternship.product.api.dto.response;

import java.util.List;

public record InventoryDto(
  int Quantity,
  List<ProductPODto> PO
) {}
