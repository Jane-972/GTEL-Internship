package org.jane.gtelinternship.product.infra.client.logicom;

import org.jane.gtelinternship.common.service.DateTimeProvider;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static org.jane.gtelinternship.common.encrypt.EncryptionUtil.encrypt;

public class TokenInterceptor implements ClientHttpRequestInterceptor {
  private final TokenProvider tokenProvider;
  private final LogicomClientConfig config;
  private final DateTimeProvider dateTimeProvider;

  public TokenInterceptor(TokenProvider tokenProvider, LogicomClientConfig config, DateTimeProvider dateTimeProvider) {
    this.tokenProvider = tokenProvider;
    this.config = config;
    this.dateTimeProvider = dateTimeProvider;
  }

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    // First try with current token
    var timestamp = dateTimeProvider.getInstant().getEpochSecond();
    var accessToken = tokenProvider.getAccessToken();
    addHeaders(request, timestamp, accessToken);

    ClientHttpResponse response = execution.execute(request, body);


    // If 401, refresh token and retry
    if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
      response.close();
      tokenProvider.refreshTokens();
      timestamp = dateTimeProvider.getInstant().getEpochSecond();
      accessToken = tokenProvider.getAccessToken();
      addHeaders(request, timestamp, accessToken);
      return execution.execute(request, body);
    }

    return response;
  }

  public void addHeaders(HttpRequest request, long timestamp, String accessToken) {
    request.getHeaders().setBearerAuth(tokenProvider.getAccessToken());
    request.getHeaders().set("Timestamp", String.valueOf(timestamp));
    request.getHeaders().set("Signature", encrypt(config.getAccessTokenKey(), accessToken + timestamp));
    request.getHeaders().set("CustomerID", String.valueOf(config.getCustomerId()));
  }
}