package org.jane.gtelinternship.product.api.controller;

import org.jane.gtelinternship.product.api.dto.response.ProductComparisonDto;
import org.jane.gtelinternship.product.domain.service.ProductComparisonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products/comparison")
public class ProductComparisonController {

  private final ProductComparisonService productComparisonService;

  public ProductComparisonController(ProductComparisonService productComparisonService) {
    this.productComparisonService = productComparisonService;
  }

  @GetMapping
  public List<ProductComparisonDto> getWooLogicomComparison() {
    return productComparisonService.getComparisonList();
  }

  @GetMapping("{sku}")
  public ResponseEntity<ProductComparisonDto> getWooLogicomComparisonBySku(@PathVariable String sku) {
    ProductComparisonDto comparison = productComparisonService.getComparisonBySku(sku);
    if (comparison == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(comparison);
  }
}
