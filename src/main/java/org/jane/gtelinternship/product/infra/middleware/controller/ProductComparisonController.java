package org.jane.gtelinternship.product.infra.middleware.controller;

import org.jane.gtelinternship.product.infra.middleware.dto.ProductComparisonDto;
import org.jane.gtelinternship.product.infra.middleware.service.ProductComparisonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comparison")
public class ProductComparisonController {

  private final ProductComparisonService productComparisonService;

  public ProductComparisonController(ProductComparisonService productComparisonService) {
    this.productComparisonService = productComparisonService;
  }

  @GetMapping("/woo-logicom")
  public List<ProductComparisonDto> getWooLogicomComparison() {
    return productComparisonService.getComparisonList();
  }
}
