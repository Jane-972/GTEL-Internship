package org.jane.gtelinternship.product.infra.client.logicom;


import jakarta.transaction.Transactional;
import org.jane.gtelinternship.product.infra.client.logicom.dto.LogicomInventoryDto;
import org.jane.gtelinternship.product.infra.client.logicom.dto.LogicomProductInventory;
import org.jane.gtelinternship.product.infra.client.woo.WooClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class InventoryService {
  private final LogicomClient logicomClient;
  private final WooClient wooClient;

  public InventoryService(LogicomClient logicomClient, WooClient wooClient) {
    this.logicomClient = logicomClient;
    this.wooClient = wooClient;
  }

  public void updateInventoryFromLogicom(List<String> skus) {
    // Get inventory from Logicom
    LogicomInventoryDto inventoryResponse = logicomClient.getProductInventory(skus);

    if (inventoryResponse.statusCode() != 1) {
      throw new RuntimeException("Failed to get inventory from Logicom: " + inventoryResponse.status());
    }

    // Update each product in WooCommerce
    for (LogicomProductInventory productInventory : inventoryResponse.message()) {
      try {
        String sku = productInventory.sku();
        Integer stockQuantity = productInventory.getInventory();

        // Update stock in WooCommerce
        wooClient.updateStockBySku(sku, stockQuantity);

        System.out.println("Updated stock for SKU: " + sku + " to quantity: " + stockQuantity);

      } catch (Exception e) {
        System.err.println("Failed to update stock for SKU: " + productInventory.sku() +
          ". Error: " + e.getMessage());
      }
    }
  }

  public void updateInventoryFromLogicom(String... skus) {
    updateInventoryFromLogicom(List.of(skus));
  }

  public LogicomInventoryDto getLogicomInventory(String... skus) {
    return logicomClient.getProductInventory(skus);
  }
}