package org.jane.gtelinternship.product.infra.client.woo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.Base64;

@Configuration
public class WooClientConfig {
    private final String baseURI;
    private final String apiKey;
    private final String apiSecret;

    public WooClientConfig(
            @Value("${woocommerce.api.url}") String baseURI,
            @Value("${woocommerce.api.key}") String apiKey,
            @Value("${woocommerce.api.secret}") String apiSecret) {
        this.baseURI = baseURI;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Bean
    RestClient wooRestClient() {
        return RestClient.builder()
                .baseUrl(baseURI)
                .defaultHeader("Authorization", "Basic " +
                        Base64.getEncoder().encodeToString((apiKey + ":" + apiSecret).getBytes()))
                .build();
    }
}
