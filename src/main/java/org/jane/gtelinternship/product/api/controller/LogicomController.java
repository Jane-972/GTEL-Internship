package org.jane.gtelinternship.product.api.controller;


import lombok.RequiredArgsConstructor;
import org.jane.gtelinternship.product.api.dto.response.ProductResponseDto;
import org.jane.gtelinternship.product.domain.model.FullProduct;
import org.jane.gtelinternship.product.domain.model.LogicomProduct;
import org.jane.gtelinternship.product.domain.service.LogicomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;


@RestController
@RequestMapping("/logicom")
@RequiredArgsConstructor
public class LogicomController {
  private final LogicomService logicomService;

  @GetMapping("/products")
  public Stream<ProductResponseDto> getFirst10ProductsFull() {
    return logicomService.getFirst10Products().map(ProductResponseDto::fromModel);
  }

  @GetMapping("/products/{sku}")
  public ProductResponseDto getProductFullBySku(@PathVariable String sku) {
    FullProduct<LogicomProduct> fullProduct = logicomService.getProductFullBySku(sku);
    return ProductResponseDto.fromModel(fullProduct);
  }

  @GetMapping("/products/all")
  public Stream<ProductResponseDto> getAllProductsFull() {
    return logicomService.getAllProductsFull().map(ProductResponseDto::fromModel);
  }
}
