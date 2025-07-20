package org.jane.gtelinternship.woo;

import org.jane.gtelinternship.product.infra.client.woo.dto.WooProductDto;
import org.jane.gtelinternship.stockupdater.domain.service.WooCommerceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class WooCommerceServiceTest {

  @Mock
  private RestTemplateBuilder restTemplateBuilder;

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private WooCommerceService wooCommerceService;


  @BeforeEach
  void setup() {
    lenient().when(restTemplateBuilder.build()).thenReturn(restTemplate);

    ReflectionTestUtils.setField(wooCommerceService, "wooUrl", "https://fakewoocommerce.com");
    ReflectionTestUtils.setField(wooCommerceService, "apiKey", "fakeKey");
    ReflectionTestUtils.setField(wooCommerceService, "apiSecret", "fakeSecret");

    ReflectionTestUtils.setField(wooCommerceService, "restTemplate", restTemplate);
  }

  @Test
  void updateStock_success() {
    WooProductDto product = new WooProductDto(
      123L,
      "SKU123",
      "Product Name",
      "Short description here",
      true,
      20,
      47.99,
      59.99,
      List.of("https://image1.jpg"),
      "Color",
      "Red",
      "Size",
      "M",
      "Material",
      "Cotton"
    );

    WooProductDto[] products = new WooProductDto[]{product};

    ResponseEntity<WooProductDto[]> getResponse = new ResponseEntity<>(products, HttpStatus.OK);
    when(restTemplate.exchange(
      contains("/wp-json/wc/v3/products?sku=SKU123"),
      eq(HttpMethod.GET),
      any(HttpEntity.class),
      eq(WooProductDto[].class)
    )).thenReturn(getResponse);

    ResponseEntity<Void> putResponse = new ResponseEntity<>(HttpStatus.OK);
    when(restTemplate.exchange(
      contains("/wp-json/wc/v3/products/123"),
      eq(HttpMethod.PUT),
      any(HttpEntity.class),
      eq(Void.class)
    )).thenReturn(putResponse);

    assertDoesNotThrow(() -> wooCommerceService.updateStock("SKU123", 10));

    verify(restTemplate).exchange(
      contains("/wp-json/wc/v3/products?sku=SKU123"),
      eq(HttpMethod.GET),
      any(HttpEntity.class),
      eq(WooProductDto[].class)
    );

    verify(restTemplate).exchange(
      contains("/wp-json/wc/v3/products/123"),
      eq(HttpMethod.PUT),
      any(HttpEntity.class),
      eq(Void.class)
    );
  }


  @Test
  void updateStock_productNotFound_throws() {
    ResponseEntity<WooProductDto[]> emptyResponse = new ResponseEntity<>(new WooProductDto[0], HttpStatus.OK);
    when(restTemplate.exchange(
      contains("/wp-json/wc/v3/products?sku="),
      eq(HttpMethod.GET),
      any(HttpEntity.class),
      eq(WooProductDto[].class)
    )).thenReturn(emptyResponse);

    RuntimeException ex = assertThrows(RuntimeException.class,
      () -> wooCommerceService.updateStock("SKU_NOT_FOUND", 10));
    assertTrue(ex.getMessage().contains("Product not found"));
  }
}
