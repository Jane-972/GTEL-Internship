package org.jane.gtelinternship.product.infra.client.logicom.dto;


import org.jane.gtelinternship.product.domain.model.ProductInventory;
import org.jane.gtelinternship.product.domain.model.ProductStock;
import org.jane.gtelinternship.product.domain.model.PurchaseOrder;
import org.openapi.example.model.InventoryItemDto;
import org.openapi.example.model.InventoryResponseDto;
import org.openapi.example.model.ProductPODto;

import java.util.List;

public class InventoryDtoMapper {
    public static ProductInventory mapToDomain(InventoryResponseDto dto) {
        if (dto == null) {
            return ProductInventory.empty();
        } else {
            return mapToModel(dto);
        }
    }

    private static ProductInventory mapToModel(InventoryResponseDto dto) {
        List<ProductStock> products = dto
                .getMessage()
                .stream()
                .map(InventoryDtoMapper::mapToModel)
                .toList();

        return new ProductInventory(products);
    }

    private static ProductStock mapToModel(InventoryItemDto dto) {
        List<PurchaseOrder> orders;

        if (dto.getPO() != null) {
            orders = dto.getPO()
                    .stream()
                    .map(InventoryDtoMapper::buildPurchaseOrder)
                    .toList();
        } else {
            orders = List.of();
        }

        return new ProductStock(dto.getSKU(), Integer.parseInt(dto.getInventory()), orders);
    }

    private static PurchaseOrder buildPurchaseOrder(ProductPODto po) {
        return new PurchaseOrder(
                "PO-" + po.hashCode(),
                Integer.parseInt(po.getQuantity()),
                po.getPoDeliveryDate()
        );
    }
}
