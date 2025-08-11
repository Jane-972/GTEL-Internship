package org.jane.gtelinternship.product.domain.service;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.jane.gtelinternship.product.domain.model.*;
import org.jane.gtelinternship.product.infra.client.logicom.LogicomClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

import static org.jane.gtelinternship.common.util.ListUtil.batchList;

@Service
@RequiredArgsConstructor
public class LogicomService {
  private final LogicomClient logicomClient;

  public Stream<FullProduct<LogicomProduct>> getFirst10Products() {
    return logicomClient.getFirst10Products();
  }

  @NotNull
  public FullProduct<LogicomProduct> getProductFullBySku(String sku) {
    return logicomClient.getProductBySku(sku);
  }

  // TODO: Not sure it's necessary.
  public Stream<FullProduct<LogicomProduct>> getAllProductsFull() {
    List<FullProduct<LogicomProduct>> allProducts = new ArrayList<>();
    String previousItemNo = null;

    // Load all products page by page
    var hasNextPage = true;
    while (hasNextPage) {
      List<FullProduct<LogicomProduct>> page = logicomClient.getProductsPage(previousItemNo).toList();

      if (page.isEmpty()) {
        hasNextPage = false;
      } else {
        allProducts.addAll(page);
        previousItemNo = page.getLast().product().getSku();
      }
    }

    // Collect all SKUs
    List<String> allSkus = allProducts.stream()
      .map(fullProduct -> fullProduct.product().getSku())
      .toList();

    // Batch SKUs
    List<List<String>> skuBatches = batchList(allSkus, 30);

    // TODO: Move and adjust
    // Fetch inventory in batches and merge results
    List<ProductStock> allProductStocks = skuBatches
      .stream()
      .map(logicomClient::getProductInventory)
      .filter(Objects::nonNull)
      .filter(inventory -> !inventory.isEmpty())
      .flatMap(inventory -> inventory.products().stream())
      .toList();

    // TODO: move and avoid dto
    // Fetch prices in batches and merge results
    Map<String, ProductPrice> skuToPrice = skuBatches
      .stream()
      .map(logicomClient::getProductPrices)
      .reduce(new HashMap<>(), (acc, map) -> {
        acc.putAll(map);
        return acc;
      });

    ProductInventory combinedInventory = new ProductInventory(allProductStocks);

    // Map products to ProductResponseDto including merged inventory and prices
    return allProducts.stream()
      .map(product -> {
        ProductPrice price = skuToPrice.get(product.product().getSku());
        ProductStock stock = combinedInventory.findBySku(product.product().getSku());

        if (price == null || stock == null) {
          return null;
        } else {
          return product;
        }
      })
      .filter(Objects::nonNull);
  }
}