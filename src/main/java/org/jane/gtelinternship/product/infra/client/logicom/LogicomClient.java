package org.jane.gtelinternship.product.infra.client.logicom;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jane.gtelinternship.product.domain.model.ProductInventory;
import org.openapi.example.model.InventoryResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.List;

import static org.jane.gtelinternship.product.infra.client.logicom.dto.InventoryDtoMapper.mapToDomain;
@Service
public class LogicomClient {
  private final RestClient logicomRestClient;

  public LogicomClient(RestClient logicomRestClient) {
    this.logicomRestClient = logicomRestClient;
  }

  public ProductInventory getProductInventory(List<String> skus) {
    // Raw JSON body
    String body = logicomRestClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/api/GetInventory")
        .queryParam("ProductID", String.join(";", skus))
        .build())
      .header("Accept", "application/json")
      .retrieve()
      .body(String.class);

    System.out.println("RAW BODY = " + body);

    // Deserialize manually
    ObjectMapper mapper = new ObjectMapper();
    InventoryResponseDto dto;
    try {
      dto = mapper.readValue(body, InventoryResponseDto.class);
    } catch (IOException e) {
      throw new RuntimeException("Failed to parse Logicom response", e);
    }

    return mapToDomain(dto);
  }
}
