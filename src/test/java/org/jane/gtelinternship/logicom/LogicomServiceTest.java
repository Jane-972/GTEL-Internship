package org.jane.gtelinternship.logicom;

import org.jane.gtelinternship.stockupdater.api.dto.logicom.LogicomProductDto;
import org.jane.gtelinternship.stockupdater.api.dto.logicom.LogicomResponseDto;
import org.jane.gtelinternship.stockupdater.domain.service.LogicomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogicomServiceTest {

  @Mock
  private RestTemplateBuilder restTemplateBuilder;

  @Mock
  private RestTemplate restTemplate;

  private LogicomService logicomService;

  @BeforeEach
  void setup() {
    // When build() is called on the builder, return the mocked RestTemplate
    when(restTemplateBuilder.build()).thenReturn(restTemplate);

    // Create service instance with mocked RestTemplateBuilder
    logicomService = new LogicomService(restTemplateBuilder);
  }

  @Test
  void fetchProducts_returnsProductList() {
    // Prepare a dummy LogicomProductDto with test data
    LogicomProductDto product = new LogicomProductDto(
      "SKU123",
      "Test Product",
      "Description",
      "Manufacturer",
      "Category",
      "0",
      "0",
      "1234567890123",
      "0",
      null, // Assuming price DTO can be null or mock separately
      0,
      "1 year",
      List.of(),
      null,
      List.of(),
      List.of()
    );

    LogicomResponseDto responseDto = new LogicomResponseDto(
      200,
      "OK",
      List.of(product),
      null
    );

    ResponseEntity<LogicomResponseDto> responseEntity =
      new ResponseEntity<>(responseDto, HttpStatus.OK);

    // Mock the restTemplate.exchange(...) call to return the dummy response
    when(restTemplate.exchange(
      anyString(),
      eq(HttpMethod.GET),
      any(HttpEntity.class),
      eq(LogicomResponseDto.class)
    )).thenReturn(responseEntity);

    // Call the method under test
    List<LogicomProductDto> products = logicomService.fetchProducts();

    // Assertions
    assertNotNull(products, "Products list should not be null");
    assertEquals(1, products.size(), "Products list size should be 1");
    assertEquals(product, products.get(0), "Product should match the mocked product");
  }

  @Test
  void fetchProducts_throwsOnNullResponse() {
    // Prepare a response entity with null body to simulate failed response
    ResponseEntity<LogicomResponseDto> responseEntity =
      new ResponseEntity<>(null, HttpStatus.OK);

    // Mock the restTemplate.exchange(...) to return null body response
    when(restTemplate.exchange(
      anyString(),
      eq(HttpMethod.GET),
      any(HttpEntity.class),
      eq(LogicomResponseDto.class)
    )).thenReturn(responseEntity);

    // Verify that calling fetchProducts throws RuntimeException
    RuntimeException ex = assertThrows(RuntimeException.class, () -> {
      logicomService.fetchProducts();
    });

    assertTrue(ex.getMessage().contains("Failed to fetch Logicom products"));
  }
}
