package org.jane.gtelinternship.product.api;

import org.jane.gtelinternship.product.api.dto.ProductInventoryResponseDto;
import org.jane.gtelinternship.product.domain.model.ProductInventory;
import org.jane.gtelinternship.product.domain.service.InventoryService;
import org.springframework.web.bind.annotation.*;

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
    return ProductInventoryResponseDto.from(inventory);
  }

  @GetMapping("/inventory/{sku}/available")
  public Boolean isProductAvailable(@PathVariable String sku) {
    return inventoryService.isProductAvailable(sku);
  }
}
