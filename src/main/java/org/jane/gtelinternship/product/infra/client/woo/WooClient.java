package org.jane.gtelinternship.product.infra.client.woo;

import org.jane.gtelinternship.common.exception.NotFoundException;
import org.jane.gtelinternship.product.infra.client.woo.dto.WooProductDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public List<WooProductDto> getAllProducts(int page, int perPage) {
        WooProductDto[] products = wooRestClient.get()
          .uri(uriBuilder -> uriBuilder
            .path("/wp-json/wc/v3/products")
            .queryParam("page", page)
            .queryParam("per_page", perPage)
            .queryParam("status", "publish")
            .build())
          .retrieve()
          .body(WooProductDto[].class);

        return products != null ? Arrays.asList(products) : List.of();
    }


    public WooProductDto updateStock(Long productId, Integer stockQuantity) {
        Map<String, Object> updateData = Map.of(
          "stock_quantity", stockQuantity,
          "manage_stock", true,
          "in_stock", stockQuantity > 0
        );

        return wooRestClient.put()
          .uri("/wp-json/wc/v3/products/{id}", productId)
          .body(updateData)
          .retrieve()
          .toEntity(WooProductDto.class)
          .getBody();
    }

    public WooProductDto updateStockBySku(String sku, Integer stockQuantity) {
        // Get the product to find its ID
        WooProductDto product = getProductBySku(sku);
        if (product != null && product.id() != null) {
            return updateStock(product.id(), stockQuantity);
        }
        throw new NotFoundException("Product not found with SKU: " + sku);
    }

    public List<WooProductDto> getAllProductsFull() {
        int page = 1;
        int perPage = 100;
        List<WooProductDto> allProducts = new ArrayList<>();

        while (true) {
            List<WooProductDto> pageProducts = getAllProducts(page, perPage);
            if (pageProducts.isEmpty()) break;
            allProducts.addAll(pageProducts);
            page++;
        }

        return allProducts;
    }
}
