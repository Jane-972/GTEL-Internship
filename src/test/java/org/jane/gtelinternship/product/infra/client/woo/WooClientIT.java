//package org.jane.gtelinternship.product.infra.client.woo;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.jane.gtelinternship.IntegrationTestBase;
import org.jane.gtelinternship.product.infra.client.woo.dto.WooProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*class WooClientIT extends IntegrationTestBase {
    private final WooProductDto sku123Product = new WooProductDto(
            123L,
            "SKU123",
            "Sample Product",
            "A short description of the product.",
            true,
            50,
            19.99,
            24.99,
            List.of(
                    "https://example.com/image1.jpg",
                    "https://example.com/image2.jpg"
            ),
            "Color",
            "Red",
            "Size",
            "Medium",
            "Material",
            "Cotton"
    );
    @Autowired
    private WooClient wooClient;

    @Test
    void shouldFetchProductBySku() {
        String sku = "SKU123";

        stubFor(WireMock.get(urlPathEqualTo("/woo/wp-json/wc/v3/products"))
                .withQueryParam("sku", equalTo(sku))
                .withBasicAuth("1234", "secret-1234") // We don't need to check all the time
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("woocommerce/SingleWooProduct.json"))
        );

        WooProductDto product = wooClient.getProductBySku(sku);

        assertEquals(sku123Product, product);
    }
}

 */