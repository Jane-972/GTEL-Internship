package org.jane.gtelinternship.product.api.controller;


import lombok.RequiredArgsConstructor;
import org.jane.gtelinternship.product.api.dto.response.ProductResponseDto;
import org.jane.gtelinternship.product.domain.model.FullProduct;
import org.jane.gtelinternship.product.domain.model.LogicomProduct;
import org.jane.gtelinternship.product.domain.service.LogicomService;
import org.jane.gtelinternship.product.domain.service.ProductCacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/logicom")
@RequiredArgsConstructor
public class LogicomController {
  // Inject cache service instead of direct service
  private final ProductCacheService productCacheService;
  private final LogicomService logicomService;

  @GetMapping("/products")
  public List<ProductResponseDto> getFirst10ProductsFull() {
    // BEFORE: logicomService.getFirst10Products() -slow-
    // AFTER: Get from cache and limit to 10 -FAST-
    return productCacheService.getAllProducts()
      .stream()
      .limit(10)
      .map(ProductResponseDto::fromModel)
      .toList();
  }

  @GetMapping("/products/{sku}")
  public ProductResponseDto getProductFullBySku(@PathVariable String sku) {
    return productCacheService.getProductBySku(sku)
      .map(ProductResponseDto::fromModel)
      .orElseGet(() -> {
        // Fallback to service only on cache miss
        FullProduct<LogicomProduct> full = logicomService.getProductFullBySku(sku);
        return ProductResponseDto.fromModel(full);
      });
  }

  @GetMapping("/products/all")
  public List<ProductResponseDto> getAllProductsFull() {
    return productCacheService.getAllProducts()
      .stream()
      .map(ProductResponseDto::fromModel)
      .toList();
  }

  // A manual refresh endpoint
  @PostMapping("/products/refresh")
  public ResponseEntity<String> refreshProducts() {
    productCacheService.forceRefresh();
    return ResponseEntity.ok("Product refresh started in background");
  }

  // Check cache health
  @GetMapping("/cache/status")
  public ProductCacheService.CacheStatus getCacheStatus() {
    return productCacheService.getCacheStatus();
  }
}

