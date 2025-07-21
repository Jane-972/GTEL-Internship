package org.jane.gtelinternship.product.infra.client.logicom;

import jakarta.transaction.Transactional;
import org.jane.gtelinternship.product.infra.client.logicom.domain.ProductInventory;
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
    ProductInventory inventoryResponse = logicomClient.getProductInventory(skus);

    inventoryResponse.products().forEach(product -> {
      try {
        // Update each product in WooCommerce
        String sku = product.sku();
        Integer stockQuantity = product.totalIncomingQuantity();

        // Update stock in WooCommerce
        wooClient.updateStockBySku(sku, stockQuantity);

        System.out.println("Updated stock for SKU: " + sku + " to quantity: " + stockQuantity);

      } catch (Exception e) {
        System.err.println("Failed to update stock for SKU: " + product.sku() +
          ". Error: " + e.getMessage());
      }
    });
  }
}
