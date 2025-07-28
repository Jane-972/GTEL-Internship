package org.jane.gtelinternship.product.infra.client.logicom;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.jane.gtelinternship.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LogicomClientIT extends IntegrationTestBase {
  @Autowired
  private LogicomClient logicomClient;

  @Test
  void shouldFetchProductInventory() {
    stubFor(WireMock.get(urlPathEqualTo("/logicom/api/GetInventory"))
      .withQueryParam("ProductID", equalTo(String.join(";", testSkus)))
      .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBodyFile("logicom/GetInventory/ProductsSuccess.json"))
    );

    var result = logicomClient.getProductInventory(testSkus);
    assertEquals(3, result.products().size());
  }

  private static final List<String> testSkus = List.of("1C7D2EA", "189T0AA");
}
