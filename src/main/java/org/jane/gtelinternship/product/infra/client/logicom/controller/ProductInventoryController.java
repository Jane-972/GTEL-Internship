package org.jane.gtelinternship.product.infra.client.logicom.controller;

import org.jane.gtelinternship.product.infra.client.logicom.domain.ProductInventory;
import org.jane.gtelinternship.product.infra.client.logicom.service.ProductInventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductInventoryController {

  private final ProductInventoryService inventoryService;

  public ProductInventoryController(ProductInventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @GetMapping("/inventory")
  public ResponseEntity<ProductInventory> getInventory(@RequestParam List<String> skus) {
    ProductInventory inventory = inventoryService.getProductInventory(skus);
    return ResponseEntity.ok(inventory);
  }


  @GetMapping("/inventory/{sku}/available")
  public ResponseEntity<Boolean> isProductAvailable(@PathVariable String sku) {
    boolean available = inventoryService.isProductAvailable(sku);
    return ResponseEntity.ok(available);
  }

}
