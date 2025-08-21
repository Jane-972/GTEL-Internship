package org.jane.gtelinternship.product.infra.client.woo;

import jakarta.annotation.Nullable;
import org.jane.gtelinternship.common.exception.NotFoundException;
import org.jane.gtelinternship.product.domain.model.FullProduct;
import org.jane.gtelinternship.product.domain.model.WooProduct;
import org.jane.gtelinternship.product.infra.client.woo.dto.WooProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;
@Service
public class WooClient {
  private final RestClient wooRestClient;
  private static final Logger log = LoggerFactory.getLogger(WooClient.class);

  public WooClient(RestClient wooRestClient) {
    this.wooRestClient = wooRestClient;
  }

  private <T> T measureTime(String operation, Supplier<T> supplier) {
    long start = System.currentTimeMillis();
    try {
      return supplier.get();
    } finally {
      long end = System.currentTimeMillis();
      log.info("{} took {} ms", operation, (end - start));
    }
  }

  @Nullable
  public FullProduct<WooProduct> getProductBySku(String sku) {
    String searchTerm = "(" + sku + ")";
    WooProductDto[] products = measureTime("GET Woo /products by SKU " + sku, () ->
      wooRestClient.get()
        .uri(uriBuilder -> uriBuilder
          .path("/wp-json/wc/v3/products")
          .queryParam("search", searchTerm)
          .build())
        .retrieve()
        .body(WooProductDto[].class)
    );

    if (products != null && products.length > 0) {
      return products[0].toFullProduct();
    }
    return null;
  }

  public Stream<FullProduct<WooProduct>> getAllProducts(int page, int perPage) {
    WooProductDto[] products = measureTime("GET Woo /products page=" + page, () ->
      wooRestClient.get()
        .uri(uriBuilder -> uriBuilder
          .path("/wp-json/wc/v3/products")
          .queryParam("page", page)
          .queryParam("per_page", perPage)
          .queryParam("status", "publish")
          .build())
        .retrieve()
        .body(WooProductDto[].class)
    );

    if (products == null || products.length == 0) {
      return Stream.empty();
    } else {
      return Arrays.stream(products)
        .map(WooProductDto::toFullProduct);
    }
  }

  public FullProduct<WooProduct> updateStock(Long productId, Integer stockQuantity) {
    Map<String, Object> updateData = Map.of(
      "stock_quantity", stockQuantity,
      "manage_stock", true,
      "in_stock", stockQuantity > 0
    );

    WooProductDto dto = measureTime("PUT Woo /products/" + productId + " stock update", () ->
      wooRestClient.put()
        .uri("/wp-json/wc/v3/products/{id}", productId)
        .body(updateData)
        .retrieve()
        .toEntity(WooProductDto.class)
        .getBody()
    );

    return (dto != null) ? dto.toFullProduct() : null;
  }

  public FullProduct<WooProduct> updateStockBySku(String sku, Integer stockQuantity) {
    // Get the product to find its ID
    FullProduct<WooProduct> fullProduct = getProductBySku(sku);
    if (fullProduct != null) {
      return updateStock(fullProduct.product().getId(), stockQuantity);
    }
    throw new NotFoundException("Product not found with SKU: " + sku);
  }

  public List<FullProduct<WooProduct>> getAllProductsFull() {
    int page = 1;
    int perPage = 100;
    List<FullProduct<WooProduct>> allProducts = new ArrayList<>();

    while (true) {
      List<FullProduct<WooProduct>> pageProducts = getAllProducts(page, perPage).toList();
      if (pageProducts.isEmpty()) break;
      allProducts.addAll(pageProducts);
      page++;
    }

    return allProducts;
  }
}
