package org.jane.gtelinternship.product.api.controller;


import lombok.RequiredArgsConstructor;
import org.jane.gtelinternship.product.api.dto.response.ProductResponseDto;
import org.jane.gtelinternship.product.domain.model.Product;
import org.jane.gtelinternship.product.domain.service.LogicomService;
import org.jane.gtelinternship.product.infra.client.logicom.LogicomClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/logicom")
@RequiredArgsConstructor
public class LogicomController {

  private final LogicomClient logicomClient;
  private final LogicomService logicomService;

  @GetMapping("/products")
  public List<Product> getFirst10Products() {
    return logicomClient.getFirst10Products();
  }

  @GetMapping("/products/full")
  public List<ProductResponseDto> getFirst10ProductsFull() {
    return logicomService.getFirst10ProductsFull();
  }

  @GetMapping("/products/full/{sku}")
  public ResponseEntity<ProductResponseDto> getProductFullBySku(@PathVariable String sku) {
    ProductResponseDto dto = logicomService.getProductFullBySku(sku);
    return ResponseEntity.ok(dto);
  }
  @GetMapping("/products/full/all")
  public List<ProductResponseDto> getAllProductsFull() {
    return logicomService.getAllProductsFull();
  }
}
