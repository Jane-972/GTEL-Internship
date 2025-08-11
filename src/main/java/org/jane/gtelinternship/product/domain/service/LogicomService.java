package org.jane.gtelinternship.product.domain.service;


import lombok.RequiredArgsConstructor;
import org.jane.gtelinternship.common.exception.NotFoundException;
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

  public Stream<Product> getFirst10Products() {
    return logicomClient.getFirst10Products().stream();
  }
  public Stream<FullProduct> getFirst10ProductsFull() {
    List<Product> products = logicomClient.getFirst10Products();
    List<String> skus = products.stream().map(Product::sku).toList();

    ProductInventory inventory = logicomClient.getProductInventory(skus);
    Map<String, ProductPrice> skuToPrice = logicomClient.getProductPrices(skus);

    return products.stream().map(product -> new FullProduct(
      product,
      skuToPrice.get(product.sku()),
      inventory.findBySku(product.sku()).availableQuantity()
    ));
  }

  public FullProduct getProductFullBySku(String sku) {
    Product product = logicomClient.getProductBySku(sku);
    if (product == null) {
      throw new NotFoundException("Product with SKU " + sku + " not found.");
    }

    ProductInventory inventory = logicomClient.getProductInventory(List.of(sku));
    Map<String, ProductPrice> prices = logicomClient.getProductPrices(List.of(sku));

    return new FullProduct(
      product,
      prices.get(sku),
      inventory.findBySku(sku).availableQuantity()
    );
  }

  // TODO: Doesn't work?
  public Stream<FullProduct> getAllProductsFull() {
    List<Product> allProducts = new ArrayList<>();
    String previousItemNo = null;

    // Load all products page by page
    var hasNextPage = true;
    while (hasNextPage) {
      List<Product> page = logicomClient.getProductsPage(previousItemNo);

      if (page.isEmpty()) {
        hasNextPage = false;
      } else {
        allProducts.addAll(page);
        previousItemNo = page.getLast().sku();
      }
    }

    // Collect all SKUs
    List<String> allSkus = allProducts.stream()
      .map(Product::sku)
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
        ProductPrice price = skuToPrice.get(product.sku());
        ProductStock stock = combinedInventory.findBySku(product.sku());

        if (price == null || stock == null) {
          return null;
        } else {
          return new FullProduct(
            product,
            price,
            stock.availableQuantity()
          );
        }
      })
      .filter(Objects::nonNull);
  }


}