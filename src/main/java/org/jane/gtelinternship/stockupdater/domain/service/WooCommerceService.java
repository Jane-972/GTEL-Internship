package org.jane.gtelinternship.stockupdater.domain.service;

import org.jane.gtelinternship.stockupdater.api.dto.WooProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Map;

@Service
public class WooCommerceService {

  private final RestTemplate restTemplate;

  @Value("${woocommerce.api.url}")
  private String wooUrl;

  @Value("${woocommerce.api.key}")
  private String apiKey;

  @Value("${woocommerce.api.secret}")
  private String apiSecret;

  public WooCommerceService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  public void updateStock(String sku, int quantity) {
    String endpoint = wooUrl + "/wp-json/wc/v3/products?sku=" + sku;

    //Fetch product by SKU from WooCommerce
    ResponseEntity<WooProductDto[]> response = restTemplate.exchange(
      endpoint,
      HttpMethod.GET,
      new HttpEntity<>(createHeaders()),
      WooProductDto[].class
    );

    WooProductDto[] products = response.getBody();
    if (products == null || products.length == 0) {
      throw new RuntimeException("Product not found in WooCommerce for SKU: " + sku);
    }

    Long productId = products[0].id();

    //Update stock
    Map<String, Object> update = Map.of("stock_quantity", quantity);
    restTemplate.exchange(
      wooUrl + "/wp-json/wc/v3/products/" + productId,
      HttpMethod.PUT,
      new HttpEntity<>(update, createHeaders()),
      Void.class
    );
  }

  private HttpHeaders createHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(apiKey, apiSecret);
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
