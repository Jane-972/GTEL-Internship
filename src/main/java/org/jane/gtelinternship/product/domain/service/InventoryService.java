package org.jane.gtelinternship.product.domain.service;

import jakarta.transaction.Transactional;
import org.jane.gtelinternship.product.domain.model.ProductInventory;
import org.jane.gtelinternship.product.domain.model.ProductStock;
import org.jane.gtelinternship.product.infra.client.logicom.LogicomClient;
import org.jane.gtelinternship.product.infra.client.woo.WooClient;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InventoryService {
  private final LogicomClient logicomClient;
  private final WooClient wooClient;
  private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

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
    List<String> failedSkus = new ArrayList<>();  // Initialize the list

    logicomClient.getProductInventory(skus).products().forEach(product -> {
      try {
        // Update each product in WooCommerce
        String sku = product.sku();
        Integer stockQuantity = product.totalIncomingQuantity();

        // Update stock in WooCommerce
        wooClient.updateStockBySku(sku, stockQuantity);
        logger.info("Updated stock for SKU: {} to quantity: {}", sku, stockQuantity);

      } catch (Exception e) {
        logger.error("Failed to update stock for SKU: {}. Error: {}", product.sku(), e.getMessage(), e);
        failedSkus.add(product.sku());
      }
    });

    if (!failedSkus.isEmpty()) {
      logger.warn("Stock update failed for SKUs: {}", failedSkus);
    }
  }
}
