package org.jane.gtelinternship.product.domain.service;

import org.jane.gtelinternship.common.exception.NotFoundException;
import org.jane.gtelinternship.product.api.dto.response.ProductComparisonDto;
import org.jane.gtelinternship.product.domain.model.FullProduct;
import org.jane.gtelinternship.product.domain.model.LogicomProduct;
import org.jane.gtelinternship.product.domain.model.WooProduct;
import org.jane.gtelinternship.product.infra.client.woo.WooClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ProductComparisonService {

  private final WooClient wooClient;
  private final ProductCacheService productCacheService; // Use cache

  public ProductComparisonService(WooClient wooClient, ProductCacheService productCacheService) {
    this.wooClient = wooClient;
    this.productCacheService = productCacheService;
  }

  // Use cached data for both Logicom and WooCommerce
  public List<ProductComparisonDto> getComparisonList() {
    List<FullProduct<WooProduct>> wooProducts = productCacheService.getAllWooProducts();
    Map<String, FullProduct<LogicomProduct>> skuToProduct = productCacheService.getAllProducts()
      .stream()
      .collect(Collectors.toMap(
        fullProduct -> fullProduct.product().getSku(),
        fullProduct -> fullProduct
      ));

    List<ProductComparisonDto> comparisonList = new ArrayList<>();

    for (FullProduct<WooProduct> wooProduct : wooProducts) {
      String sku = extractSkuFromName(wooProduct.product().getName());
      if (sku == null) continue;

      FullProduct<LogicomProduct> logicomProduct = skuToProduct.get(sku);
      if (logicomProduct == null) continue;

      double wooPrice = wooProduct.price().amount();
      double logicomPrice = logicomProduct.price().amount();

      double difference = Math.round((wooPrice - logicomPrice) * 100.0) / 100.0;
      String status = (difference == 0.0) ? "synced" : "to_update";

      comparisonList.add(new ProductComparisonDto(
        wooProduct.product().getName(),
        sku,
        wooPrice,
        logicomPrice,
        difference,
        status
      ));
    }

    return comparisonList;
  }

  public ProductComparisonDto getComparisonBySku(String sku) {
    FullProduct<WooProduct> wooProduct = wooClient.getProductBySku(sku);

    // Check cache first, fallback to API if needed
    Optional<FullProduct<LogicomProduct>> logicomOptional = productCacheService.getAllProducts()
      .stream()
      .filter(p -> p.product().getSku().equals(sku))
      .findFirst();

    if (wooProduct == null) {
      throw new NotFoundException("Product with SKU " + sku + " not found in WooCommerce.");
    }

    if (logicomOptional.isEmpty()) {
      throw new NotFoundException("Product with SKU " + sku + " not found in LogiCom.");
    }

    FullProduct<LogicomProduct> logicomProduct = logicomOptional.get();

    double wooPrice = wooProduct.price().amount();
    double logicomPrice = logicomProduct.price().amount();

    double difference = Math.round((wooPrice - logicomPrice) * 100.0) / 100.0;
    String status = difference == 0.0 ? "synced" : "to_update";

    return new ProductComparisonDto(
      wooProduct.product().getName(),
      sku,
      wooPrice,
      logicomPrice,
      difference,
      status
    );
  }

  private String extractSkuFromName(String name) {
    if (name == null) return null;
    Pattern pattern = Pattern.compile("\\(([^)]+)\\)$");
    Matcher matcher = pattern.matcher(name);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }
}

