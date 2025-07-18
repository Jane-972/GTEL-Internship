package org.jane.gtelinternship.stockupdater.api.dto.logicom;

import java.util.List;

public record LogicomInventoryDto(
  int Quantity,
  List<LogicomPurchaseOrderDto> PO
) {}