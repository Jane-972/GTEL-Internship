package org.jane.gtelinternship.product.infra.client.woo;

import org.jane.gtelinternship.product.infra.client.woo.dto.WooProductDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WooClient {
    private final RestClient wooRestClient;

    public WooClient(RestClient wooRestClient) {
        this.wooRestClient = wooRestClient;
    }

    public WooProductDto getProductBySku(String sku) {
        return wooRestClient.get()
                .uri("/wp-json/wc/v3/products?sku={sku}", sku)
                .retrieve()
                .toEntity(WooProductDto.class)
                .getBody();
    }
}
