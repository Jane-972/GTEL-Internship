package org.jane.gtelinternship.product.infra.client.logicom;

import org.jane.gtelinternship.product.infra.client.logicom.domain.ProductInventory;
import org.jane.gtelinternship.product.infra.client.logicom.dto.LogicomInventoryDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.jane.gtelinternship.product.infra.client.logicom.LogicomHeadersUtil.createLogicomHeaders;

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
        .path("/api/GetInventory/")
        .queryParam("ProductID", String.join(";", skus))
        .build())
      .headers(_ -> createLogicomHeaders(config))
      .retrieve()
      .body(LogicomInventoryDto.class);

    return mapToDomain(dto);
  }

  private ProductInventory mapToDomain(LogicomInventoryDto dto) {
    if (dto == null || dto.message() == null) {
      return ProductInventory.empty();
    } else {
      return dto.toModel();
    }
  }
}
