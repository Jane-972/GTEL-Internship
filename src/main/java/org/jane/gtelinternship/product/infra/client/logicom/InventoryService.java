package org.jane.gtelinternship.product.infra.client.logicom;


import jakarta.transaction.Transactional;
import org.jane.gtelinternship.product.infra.client.logicom.domain.ProductInventory;
import org.jane.gtelinternship.product.infra.client.logicom.domain.ProductStock;
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
    ProductInventory inventory = logicomClient.getProductInventory(skus);

    for (ProductStock stock : inventory.products()) {
      try {
        String sku = stock.sku();
        Integer stockQuantity = stock.availableQuantity();

        wooClient.updateStockBySku(sku, stockQuantity);
        System.out.println("Updated stock for SKU: " + sku + " to quantity: " + stockQuantity);
      } catch (Exception e) {
        System.err.println("Failed to update stock for SKU: " + stock.sku() + ". Error: " + e.getMessage());
      }
    }
  }


  public void updateInventoryFromLogicom(String... skus) {
    updateInventoryFromLogicom(List.of(skus));
  }

}