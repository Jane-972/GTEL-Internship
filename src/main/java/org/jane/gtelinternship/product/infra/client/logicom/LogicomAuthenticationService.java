package org.jane.gtelinternship.product.infra.client.logicom;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class LogicomAuthenticationService {
  private final LogicomClientConfig config;

  public LogicomAuthenticationService(LogicomClientConfig config) {
    this.config = config;
  }

  public HttpHeaders buildHeaders() {
    return HttpHeaders.EMPTY;
  }
}
