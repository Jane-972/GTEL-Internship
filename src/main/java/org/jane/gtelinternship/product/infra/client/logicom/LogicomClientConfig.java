package org.jane.gtelinternship.product.infra.client.logicom;

import lombok.Getter;
import org.jane.gtelinternship.common.service.DateTimeProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.List;

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
      .messageConverters(converters -> {
        // Find and remove the default String converter that handles text/plain
        converters.removeIf(c -> c instanceof StringHttpMessageConverter);

        // Create a Jackson converter that also handles text/plain
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
        jacksonConverter.setSupportedMediaTypes(List.of(
          MediaType.APPLICATION_JSON,
          MediaType.TEXT_PLAIN,
          new MediaType("text", "plain", StandardCharsets.UTF_8)
        ));


        // Add our modified converter
        converters.add(0, jacksonConverter);
      })
      .build();
  }
}