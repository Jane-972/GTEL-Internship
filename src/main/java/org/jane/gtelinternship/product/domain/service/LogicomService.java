package org.jane.gtelinternship.product.domain.service;


import lombok.RequiredArgsConstructor;
import org.jane.gtelinternship.common.exception.NotFoundException;
import org.jane.gtelinternship.product.api.dto.response.PriceDto;
import org.jane.gtelinternship.product.api.dto.response.ProductResponseDto;
import org.jane.gtelinternship.product.domain.model.Product;
import org.jane.gtelinternship.product.domain.model.ProductInventory;
import org.jane.gtelinternship.product.infra.client.logicom.LogicomClient;
import org.jane.gtelinternship.product.infra.client.logicom.TokenProvider;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LogicomService {
  private final LogicomClient logicomClient;
  private final TokenProvider tokenProvider;

  public List<ProductResponseDto> getFirst10ProductsFull() {
    List<Product> products = logicomClient.getFirst10Products();
    List<String> skus = products.stream().map(Product::sku).toList();

    ProductInventory inventory = logicomClient.getProductInventory(skus);
    Map<String, PriceDto> prices = logicomClient.getProductPrices(skus);

    return products.stream().map(product -> new ProductResponseDto(
      product.sku(),
      product.name(),
      product.brand(),
      prices.get(product.sku()),
      inventory.findBySku(product.sku())
    )).toList();
  }

  public ProductResponseDto getProductFullBySku(String sku) {
    Product product = logicomClient.getProductBySku(sku);
    if (product == null) {
      throw new NotFoundException("Product with SKU " + sku + " not found.");
    }

    ProductInventory inventory = logicomClient.getProductInventory(List.of(sku));
    Map<String, PriceDto> prices = logicomClient.getProductPrices(List.of(sku));

    return new ProductResponseDto(
      product.sku(),
      product.name(),
      product.brand(),
      prices.get(sku),
      inventory.findBySku(sku)
    );
  }
  public List<ProductResponseDto> getAllProductsFull() {
    tokenProvider.refreshTokens(); // Tried to force the refresh before looping

    List<Product> allProducts = new ArrayList<>();
    String previousItemNo = null;

    while (true) {
      List<Product> page = logicomClient.getProductsPage(previousItemNo);

      if (page.isEmpty()) break;

      allProducts.addAll(page);
      previousItemNo = page.get(page.size() - 1).sku();
    }

    // Get SKUs for all products
    List<String> allSkus = allProducts.stream()
      .map(Product::sku)
      .toList();

    ProductInventory inventory = logicomClient.getProductInventory(allSkus);
    Map<String, PriceDto> prices = logicomClient.getProductPrices(allSkus);

    return allProducts.stream()
      .map(product -> new ProductResponseDto(
        product.sku(),
        product.name(),
        product.brand(),
        prices.get(product.sku()),
        inventory.findBySku(product.sku())
      ))
      .toList();
  }

}