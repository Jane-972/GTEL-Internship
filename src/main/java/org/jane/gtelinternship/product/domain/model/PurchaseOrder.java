package org.jane.gtelinternship.product.domain.model;

import java.time.LocalDate;

public record PurchaseOrder(
        String orderNumber,
        int quantity,
        LocalDate expectedDate
) {

}
