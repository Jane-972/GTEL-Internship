package org.jane.gtelinternship.product.infra.client.logicom;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.jane.gtelinternship.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenProviderIT extends IntegrationTestBase {
  @Autowired
  private TokenProvider tokenProvider;

  @BeforeEach
  void setUp() {
    tokenProvider.clear();
  }

  @Test
  void shouldGetAccessToken() {
    String expectedToken = "my-access-token";
    stubAccessTokenResponse(expectedToken);

    var accessToken = tokenProvider.getAccessToken();
    assertEquals(expectedToken, accessToken);
  }

  @Test
  void shouldNotRefreshIfStillValid() {
    String expectedToken = "my-access-token";
    stubAccessTokenResponse(expectedToken);

    var accessToken = tokenProvider.getAccessToken();
    assertEquals(expectedToken, accessToken);

    stubAccessTokenResponse("dummy-token");
    Mockito.when(dateTimeProvider.getInstant()).thenReturn(now.plusMillis(54_000));
    var newAccessToken = tokenProvider.getAccessToken();
    assertEquals(expectedToken, newAccessToken);
  }

  @Test
  void shouldRefreshTokenIfNoLongerValid() {
    String expectedToken1 = "my-access-token-1";
    stubAccessTokenResponse(expectedToken1);

    var accessToken = tokenProvider.getAccessToken();
    assertEquals(expectedToken1, accessToken);

    String expectedToken2 = "my-access-token-2";
    stubNewAccessTokenResponse(expectedToken2);
    Mockito.when(dateTimeProvider.getInstant()).thenReturn(now.plusMillis(60_000)); // Simulate token expiration after 60 seconds

    var newAccessToken = tokenProvider.getAccessToken();
    assertEquals(expectedToken2, newAccessToken);
  }

  private void stubAccessTokenResponse(String expectedToken) {
    stubFor(WireMock.get(urlPathEqualTo("/logicom/GenerateAccessToken"))
      .withHeader("CustomerId", equalTo("1234"))
      .withHeader("Timestamp", equalTo("1753127306"))
      .withHeader("BCode", equalTo("DbbOADPpEMhYaIFZy/nR118Ln81cFS3EY1wC6vh8Xtc="))
      .withHeader("GenerateSignature", equalTo("6a2KmY0uYmZVrZIQTjXwzs/qvnjdhyqP8cPiDaqwJpr1Ycwe96X/6nDGJawZ2wsO"))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(expectedToken))
    );
  }

  private void stubNewAccessTokenResponse(String expectedToken) {
    stubFor(WireMock.get(urlPathEqualTo("/logicom/GenerateAccessToken"))
      .withHeader("CustomerId", equalTo("1234"))
      .withHeader("Timestamp", equalTo("1753127366"))
      .withHeader("BCode", equalTo("DbbOADPpEMhYaIFZy/nR118Ln81cFS3EY1wC6vh8Xtc="))
      .withHeader("GenerateSignature", equalTo("6a2KmY0uYmZVrZIQTjXwzl3oNcReNhsQXoNAdGZw+Tr1Ycwe96X/6nDGJawZ2wsO"))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(expectedToken))
    );
  }
}
