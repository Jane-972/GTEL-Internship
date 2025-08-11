package org.jane.gtelinternship.product.infra.client.logicom;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jane.gtelinternship.product.api.dto.response.GetProductPricesResponseDto;
import org.jane.gtelinternship.product.api.dto.response.PriceDto;
import org.jane.gtelinternship.product.domain.model.Product;
import org.jane.gtelinternship.product.domain.model.ProductInventory;
import org.jane.gtelinternship.product.api.dto.response.GetProductsResponseDto;
import org.jane.gtelinternship.product.infra.client.logicom.mapper.PriceDtoMapper;
import org.jane.gtelinternship.product.infra.client.logicom.mapper.ProductDtoMapper;
import org.openapi.example.model.InventoryResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static org.jane.gtelinternship.product.infra.client.logicom.mapper.InventoryDtoMapper.mapToDomain;

@Service
public class LogicomClient {
  private final RestClient logicomRestClient;
  private final ObjectMapper objectMapper;


  public LogicomClient(RestClient logicomRestClient, ObjectMapper objectMapper) {
    this.logicomRestClient = logicomRestClient;
    this.objectMapper = objectMapper;
  }
  private static final Logger log = LoggerFactory.getLogger(LogicomClient.class);


  @SneakyThrows
  public ProductInventory getProductInventory(List<String> skus) {
    var body = logicomRestClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/api/GetInventory")
        .queryParam("ProductID", String.join(";", skus))
        .build())
      .retrieve()
      .body(String.class);
    String productIds = String.join(";", skus);
    log.info("Calling /api/GetInventory with ProductID={}", productIds);
    return mapToDomain(objectMapper.readValue(body, InventoryResponseDto.class));
  }
  @SneakyThrows
  public List<Product> getFirst10Products() {
    var body = logicomRestClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/api/GetProducts")
        .build())
      .retrieve()
      .body(String.class);

    var responseDto = objectMapper.readValue(body, GetProductsResponseDto.class);
    return ProductDtoMapper.mapToDomain(responseDto.Message());
  }

  @SneakyThrows
  public Map<String, PriceDto> getProductPrices(List<String> skus) {
    var response = logicomRestClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/api/GetPrice")
        .queryParam("ProductID", String.join(";", skus))
        .build())
      .retrieve()
      .body(String.class);

    var prices = objectMapper.readValue(response, GetProductPricesResponseDto.class);
    return prices.Message().stream()
      .map(PriceDtoMapper::from)
      .collect(Collectors.toMap(PriceDto::sku, Function.identity()));

  }
  @SneakyThrows
  public Product getProductBySku(String sku) {
    var response = logicomRestClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/api/GetProducts")
        .queryParam("ProductId", sku)
        .build())
      .retrieve()
      .body(String.class);

    var dto = objectMapper.readValue(response, GetProductsResponseDto.class);
    var productList = ProductDtoMapper.mapToDomain(dto.Message());

    return productList.isEmpty() ? null : productList.get(0);
  }
  @SneakyThrows
  public List<Product> getProductsPage(String previousItemNo) {
    var uriBuilder = logicomRestClient.get()
      .uri(uri -> {
        var builder = uri.path("/api/GetProducts");
        if (previousItemNo != null && !previousItemNo.isBlank()) {
          builder.queryParam("PreviousItemNo", previousItemNo);
        }
        return builder.build();
      })
      .retrieve()
      .body(String.class);

    var responseDto = objectMapper.readValue(uriBuilder, GetProductsResponseDto.class);
    return ProductDtoMapper.mapToDomain(responseDto.Message());
  }

}
