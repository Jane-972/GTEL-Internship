package org.jane.gtelinternship.product.infra.client.logicom;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jane.gtelinternship.logicom.InventoryResponseDto;
import org.jane.gtelinternship.product.domain.model.FullProduct;
import org.jane.gtelinternship.product.domain.model.LogicomProduct;
import org.jane.gtelinternship.product.domain.model.ProductInventory;
import org.jane.gtelinternship.product.domain.model.ProductPrice;
import org.jane.gtelinternship.product.infra.client.logicom.dto.GetProductPricesResponseDto;
import org.jane.gtelinternship.product.infra.client.logicom.dto.GetProductsResponseDto;
import org.jane.gtelinternship.product.infra.client.logicom.mapper.PriceDtoMapper;
import org.jane.gtelinternship.product.infra.client.logicom.mapper.ProductDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.jane.gtelinternship.product.infra.client.logicom.mapper.InventoryDtoMapper.mapToDomain;

@Service
@RequiredArgsConstructor
public class LogicomClient {
  private final RestClient logicomRestClient;
  private final ObjectMapper objectMapper;

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
  public Stream<FullProduct<LogicomProduct>> getFirst10Products() {
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
  public Map<String, ProductPrice> getProductPrices(List<String> skus) {
    var response = logicomRestClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/api/GetPrice")
        .queryParam("ProductID", String.join(";", skus))
        .build())
      .retrieve()
      .body(String.class);

    var prices = objectMapper.readValue(response, GetProductPricesResponseDto.class);

    return prices.Message()
      .stream()
      .collect(
        HashMap::new,
        (map, elem) -> map.put(
          elem.SKU(),
          PriceDtoMapper.from(elem)
        ),
        HashMap::putAll
      );
  }

  @SneakyThrows
  @Nullable
  public FullProduct<LogicomProduct> getProductBySku(String sku) {
    var response = logicomRestClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/api/GetProducts")
        .queryParam("ProductId", sku)
        .build())
      .retrieve()
      .body(String.class);

    var dto = objectMapper.readValue(response, GetProductsResponseDto.class);
    var productList = ProductDtoMapper.mapToDomain(dto.Message()).toList();

    return productList.isEmpty() ? null : productList.getFirst();
  }

  @SneakyThrows
  public Stream<FullProduct<LogicomProduct>> getProductsPage(String previousItemNo) {
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
