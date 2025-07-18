package org.jane.gtelinternship.stockupdater.domain.service;

import org.jane.gtelinternship.stockupdater.api.dto.logicom.LogicomProductDto;
import org.jane.gtelinternship.stockupdater.api.dto.logicom.LogicomResponseDto;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;

@Service
public class LogicomService {

  private final RestTemplate restTemplate;

  //Credentials
  private final String accessToken = "ACCESS_TOKEN";
  private final String customerId = "CUSTOMER_ID";
  private final String signature = "GENERATED_SIGNATURE";

  public LogicomService(RestTemplateBuilder builder) {
    this.restTemplate = builder.build();
  }

  public List<LogicomProductDto> fetchProducts() {
    String url = "https://quickconnect.logicompartners.com/api/GetProducts?Currency=MAD";

    HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

    ResponseEntity<LogicomResponseDto> response = restTemplate.exchange(
      url, HttpMethod.GET, entity, LogicomResponseDto.class
    );

    LogicomResponseDto body = response.getBody();
    if (body == null || body.message() == null) {
      throw new RuntimeException("Failed to fetch Logicom products or empty response");
    }
    return body.message();
  }

  private HttpHeaders createHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", accessToken);
    headers.set("Timestamp", String.valueOf(Instant.now().getEpochSecond()));
    headers.set("Signature", signature);
    headers.set("CustomerID", customerId);
    return headers;
  }
}

