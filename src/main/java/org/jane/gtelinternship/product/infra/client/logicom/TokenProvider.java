package org.jane.gtelinternship.product.infra.client.logicom;

import jakarta.annotation.Nullable;
import org.jane.gtelinternship.common.service.DateTimeProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;

import static org.jane.gtelinternship.common.encrypt.EncryptionUtil.encrypt;

@Service
public class TokenProvider {
  private static final long MAX_TOKEN_LIFE_MS = 55 * 1000; // 60 seconds minus 5 seconds buffer. In milliseconds.
  private final LogicomClientConfig config;
  private final DateTimeProvider dateTimeProvider;
  private final RestClient restClient = RestClient.create();
  @Nullable
  private String accessToken;
  private Long lastRefreshMillis;

  public TokenProvider(LogicomClientConfig config, DateTimeProvider dateTimeProvider) {
    this.config = config;
    this.dateTimeProvider = dateTimeProvider;
  }

  public synchronized String getAccessToken() {
    if (accessToken == null || lastRefreshMillis == null || (dateTimeProvider.getInstant().toEpochMilli() - lastRefreshMillis) > MAX_TOKEN_LIFE_MS) {
      refreshTokens();
    }
    return accessToken;
  }

  public void clear() {
    this.accessToken = null;
    this.lastRefreshMillis = null;
  }

  public synchronized void refreshTokens() {
    try {
      String bCode = encrypt(config.getAccessTokenKey(), config.getConsumerKey() + ";" + config.getConsumerSecret());

      Instant instant = dateTimeProvider.getInstant();
      long now = instant.getEpochSecond(); // Convert to seconds
      String timeStamp = String.valueOf(now);
      String signature = encrypt(config.getAccessTokenKey(), config.getConsumerKey() + config.getCustomerId() + timeStamp + ";" + config.getConsumerSecret());

      this.accessToken = restClient.get()
        .uri(config.getBaseUrl() + "/GenerateAccessToken")
        .header("CustomerId", config.getCustomerId().toString())
        .header("Timestamp", timeStamp)
        .header("BCode", bCode)
        .header("GenerateSignature", signature)
        .retrieve()
        .body(String.class);

      this.lastRefreshMillis = instant.toEpochMilli();
    } catch (Exception e) {
      accessToken = null;
      lastRefreshMillis = null;
      throw new RuntimeException("Failed to refresh access token", e);
    }
  }
}
