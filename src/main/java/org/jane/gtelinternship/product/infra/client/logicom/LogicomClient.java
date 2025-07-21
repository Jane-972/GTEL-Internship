package org.jane.gtelinternship.product.infra.client.logicom;

import org.jane.gtelinternship.product.domain.model.ProductInventory;
import org.openapi.example.model.InventoryResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.jane.gtelinternship.product.infra.client.logicom.LogicomHeadersUtil.createLogicomHeaders;
import static org.jane.gtelinternship.product.infra.client.logicom.dto.InventoryDtoMapper.mapToDomain;

@Service
public class LogicomClient {
  private final RestClient logicomRestClient;
  private final LogicomClientConfig config;

  public LogicomClient(RestClient logicomRestClient, LogicomClientConfig config) {
    this.logicomRestClient = logicomRestClient;
    this.config = config;
  }

  public ProductInventory getProductInventory(List<String> skus) {
    var dto = logicomRestClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/api/GetInventory")
        .queryParam("ProductID", String.join(";", skus))
        .build())
      .headers(headers -> createLogicomHeaders(config))
      .retrieve()
      .body(InventoryResponseDto.class);

    return mapToDomain(dto);
  }
}
