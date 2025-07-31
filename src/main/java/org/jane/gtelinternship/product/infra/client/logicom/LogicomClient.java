package org.jane.gtelinternship.product.infra.client.logicom;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jane.gtelinternship.product.domain.model.ProductInventory;
import org.openapi.example.model.InventoryResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.jane.gtelinternship.product.infra.client.logicom.dto.InventoryDtoMapper.mapToDomain;

@Service
public class LogicomClient {
  private final RestClient logicomRestClient;
  private final ObjectMapper objectMapper;


  public LogicomClient(RestClient logicomRestClient, ObjectMapper objectMapper) {
    this.logicomRestClient = logicomRestClient;
    this.objectMapper = objectMapper;
  }

  @SneakyThrows
  public ProductInventory getProductInventory(List<String> skus) {
    var body = logicomRestClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/api/GetInventory")
        .queryParam("ProductID", String.join(";", skus))
        .build())
      .retrieve()
      .body(String.class);

    return mapToDomain(objectMapper.readValue(body, InventoryResponseDto.class));
  }
}
