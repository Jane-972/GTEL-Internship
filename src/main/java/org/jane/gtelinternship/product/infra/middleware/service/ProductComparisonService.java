package org.jane.gtelinternship.product.infra.middleware.service;

import org.jane.gtelinternship.product.api.dto.response.ProductResponseDto;
import org.jane.gtelinternship.product.domain.service.LogicomService;
import org.jane.gtelinternship.product.infra.client.woo.WooClient;
import org.jane.gtelinternship.product.infra.client.woo.dto.WooProductDto;
import org.jane.gtelinternship.product.infra.middleware.dto.ProductComparisonDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ProductComparisonService {

  private final WooClient wooClient;
  private final LogicomService logicomService;

  public ProductComparisonService(WooClient wooClient, LogicomService logicomService) {
    this.wooClient = wooClient;
    this.logicomService = logicomService;
  }

  public List<ProductComparisonDto> getComparisonList() {
    List<WooProductDto> wooProducts = wooClient.getAllProductsFull();
    List<ProductResponseDto> logicomProducts = logicomService.getAllProductsFull();

    Map<String, ProductResponseDto> logicomMap = logicomProducts.stream()
      .collect(Collectors.toMap(ProductResponseDto::sku, p -> p));

    List<ProductComparisonDto> comparisonList = new ArrayList<>();

    for (WooProductDto wooProduct : wooProducts) {
      String sku = extractSkuFromName(wooProduct.name());
      if (sku == null) continue;

      ProductResponseDto logicomProduct = logicomMap.get(sku);

      double wooPrice = wooProduct.price() != null ? wooProduct.price() : 0.0;
      double logicomPrice = parsePrice(logicomProduct);

      double difference = Math.round((wooPrice - logicomPrice) * 100.0) / 100.0;
      String status = (difference == 0.0) ? "synced" : "to_update";

      comparisonList.add(new ProductComparisonDto(
        wooProduct.name(),
        sku,
        wooPrice,
        logicomPrice,
        difference,
        status
      ));
    }

    return comparisonList;
  }

  /**
   * Parse Logicom's PriceExclVAT (removes commas and handles nulls)
   */
  private double parsePrice(ProductResponseDto logicomProduct) {
    if (logicomProduct == null || logicomProduct.price() == null) {
      return 0.0;
    }
    try {
      return Double.parseDouble(
        logicomProduct.price().PriceExclVAT().replace(",", "")
      );
    } catch (NumberFormatException e) {
      return 0.0;
    }
  }


  public ProductComparisonDto getComparisonBySku(String sku) {
    WooProductDto wooProduct = wooClient.getProductBySku(sku);
    ProductResponseDto logicomProduct = logicomService.getProductFullBySku(sku);

    if (wooProduct == null && logicomProduct == null) {
      return null;
    }

    double wooPrice = wooProduct != null && wooProduct.price() != null ? wooProduct.price() : 0.0;
    double logicomPrice = 0.0;

    if (logicomProduct != null && logicomProduct.price() != null) {
      try {
        logicomPrice = Double.parseDouble(
          logicomProduct.price().PriceExclVAT().replace(",", "")
        );
      } catch (NumberFormatException ignored) {}
    }

    double difference = Math.round((wooPrice - logicomPrice) * 100.0) / 100.0;
    String status = difference == 0.0 ? "synced" : "to_update";

    return new ProductComparisonDto(
      wooProduct != null ? wooProduct.name() : logicomProduct.name(),
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
