package org.jane.gtelinternship.product.infra.client.logicom;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class TokenRefreshInterceptor implements ClientHttpRequestInterceptor {
  private final TokenProvider tokenProvider;

  public TokenRefreshInterceptor(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    // First try with current token
    request.getHeaders().setBearerAuth(tokenProvider.getAccessToken());
    ClientHttpResponse response = execution.execute(request, body);

    // If 401, refresh token and retry
    if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
      response.close();
      tokenProvider.refreshTokens();
      request.getHeaders().setBearerAuth(tokenProvider.getAccessToken());
      return execution.execute(request, body);
    }

    return response;
  }
}