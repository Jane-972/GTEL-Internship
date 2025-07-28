package org.jane.gtelinternship.product.domain.service;

import jakarta.transaction.Transactional;
import org.jane.gtelinternship.product.domain.model.ProductInventory;
import org.jane.gtelinternship.product.domain.model.ProductStock;
import org.jane.gtelinternship.product.infra.client.logicom.LogicomClient;
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

  public ProductInventory getProductInventory(List<String> skus) {
    return logicomClient.getProductInventory(skus);
  }

  public boolean isProductAvailable(String sku) {
    ProductInventory inventory = getProductInventory(List.of(sku));
    ProductStock stock = inventory.findBySku(sku);
    return stock != null && stock.isInStock();
  }

  public int getAvailableQuantity(String sku) {
    ProductInventory inventory = getProductInventory(List.of(sku));
    ProductStock stock = inventory.findBySku(sku);
    return stock != null ? stock.availableQuantity() : 0;
  }

  public void updateInventoryFromLogicom(List<String> skus) {
    // Get inventory from Logicom
    logicomClient.getProductInventory(skus).products().forEach(product -> {
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
        // TODO: What do we do in case of failure?
      }
    });
  }
}
