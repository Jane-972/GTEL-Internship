package org.jane.gtelinternship.product.infra.client.logicom;

import lombok.Getter;
import org.jane.gtelinternship.common.service.DateTimeProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@Getter
public class LogicomClientConfig {
  private final String baseUrl;
  private final Integer customerId;
  private final String consumerKey;
  private final String consumerSecret;
  private final String accessTokenKey;

  public LogicomClientConfig(
    @Value("${logicom.api.url}") String baseUrl,
    @Value("${logicom.api.consumer-key}") String consumerKey,
    @Value("${logicom.api.consumer-secret}") String consumerSecret,
    @Value("${logicom.api.access-token-key}") String accessTokenKey,
    @Value("${logicom.api.customer-id}") Integer customerId
  ) {
    this.baseUrl = baseUrl;
    this.consumerKey = consumerKey;
    this.consumerSecret = consumerSecret;
    this.customerId = customerId;
    this.accessTokenKey = accessTokenKey;
  }

  @Bean
  public RestClient logicomRestClient(TokenProvider tokenProvider, DateTimeProvider dateTimeProvider) {
    return RestClient.builder()
      .baseUrl(baseUrl)
      .requestInterceptor(new TokenInterceptor(tokenProvider, this, dateTimeProvider))
      .build();
  }
}