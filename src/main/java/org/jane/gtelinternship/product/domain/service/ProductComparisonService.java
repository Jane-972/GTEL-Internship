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
    List<FullProduct<WooProduct>> wooProducts = wooClient.getAllProductsFull();

    Map<String, FullProduct<LogicomProduct>> skuToProduct = logicomService.getAllProductsFull()
      .collect(Collectors.toMap((FullProduct<LogicomProduct> fullProduct) -> fullProduct.product().getSku(), p -> p));

    List<ProductComparisonDto> comparisonList = new ArrayList<>();

    for (FullProduct<WooProduct> wooProduct : wooProducts) {
      String sku = extractSkuFromName(wooProduct.product().getName());
      if (sku == null) continue;

      double wooPrice = wooProduct.price().amount(); // TODO: Think about currency conversion
      double logicomPrice =  skuToProduct.get(sku).price().amount();

      double difference = Math.round((wooPrice - logicomPrice) * 100.0) / 100.0;
      String status = (difference == 0.0) ? "synced" : "to_update";

      comparisonList.add(new ProductComparisonDto( // TODO: remove dto
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
    FullProduct<LogicomProduct> logicomProduct = logicomService.getProductFullBySku(sku);

    if (wooProduct == null) {
      throw new NotFoundException("Product with SKU " + sku + " not found in WooCommerce.");
    } else if (logicomProduct == null) {
      throw new NotFoundException("Product with SKU " + sku + " not found in LogiCom.");
    } else {
      double wooPrice = wooProduct.price().amount();
      double logicomPrice = logicomProduct.price().amount();

      // TODO: Add currency conversion if necessary
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
