package org.jane.gtelinternship.product.infra.client.logicom.service;

import org.jane.gtelinternship.product.infra.client.logicom.LogicomClient;
import org.jane.gtelinternship.product.infra.client.logicom.domain.ProductInventory;
import org.jane.gtelinternship.product.infra.client.logicom.domain.ProductStock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInventoryService {

  private final LogicomClient logicomClient;

  public ProductInventoryService(LogicomClient logicomClient) {
    this.logicomClient = logicomClient;
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
}
