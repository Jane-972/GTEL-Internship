package org.jane.gtelinternship.product.infra.client.logicom;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@Getter
public class LogicomClientConfig {
  private final String baseUrl;
  private final String accessToken;
  private final String accessTokenKey;
  private final Integer customerId;

  public LogicomClientConfig(
    @Value("${logicom.api.url}") String baseUrl,
    @Value("${logicom.api.access-token}") String accessToken,
    @Value("${logicom.api.access-token-key}") String accessTokenKey,
    @Value("${logicom.api.customer-id}") Integer customerId) {
    this.baseUrl = baseUrl;
    this.accessToken = accessToken;
    this.accessTokenKey = accessTokenKey;
    this.customerId = customerId;
  }

  @Bean
  RestClient logicomRestClient() {
    return RestClient.builder()
      .baseUrl(baseUrl)
      .build();
  }
}