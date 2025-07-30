package org.jane.gtelinternship.product.api;

import org.jane.gtelinternship.product.domain.model.ProductInventory;
import org.jane.gtelinternship.product.domain.service.InventoryService;
import org.jane.gtelinternship.product.infra.client.logicom.dto.ProductInventoryMapper;
import org.jane.gtelinternship.product.infra.client.logicom.dto.ProductInventoryResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductInventoryController {
  private final InventoryService inventoryService;

  public ProductInventoryController(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @GetMapping("/inventory")
  public ProductInventoryResponseDto getInventory(@RequestParam List<String> skus) {
    ProductInventory inventory = inventoryService.getProductInventory(skus);
    return ProductInventoryMapper.toDto(inventory);
  }

  @GetMapping("/inventory/{sku}/available")
  public Boolean isProductAvailable(@PathVariable String sku) {
    return inventoryService.isProductAvailable(sku);
  }
}
