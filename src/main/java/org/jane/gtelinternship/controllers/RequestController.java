package org.jane.gtelinternship.controllers;

import lombok.RequiredArgsConstructor;
import org.jane.gtelinternship.DTOs.SendRequestDto;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@RequestMapping("/api/requetes")
@RequiredArgsConstructor
public class RequestController {

  private final RestTemplate restTemplate = new RestTemplate();

  @PostMapping
  public ResponseEntity<?> sendRequete(@RequestBody SendRequestDto dto) {
    try {
      // Convert headers list to HttpHeaders
      HttpHeaders headers = new HttpHeaders();
      for (SendRequestDto.HeaderDto header : dto.getHeaders()) {
        headers.set(header.getKey(), header.getValue());
      }

      HttpMethod method = switch (dto.getTypeRequete()) {
        case 2 -> HttpMethod.POST;
        case 3 -> HttpMethod.PUT;
        case 4 -> HttpMethod.PATCH;
        case 5 -> HttpMethod.DELETE;
        default -> HttpMethod.GET;
      };

      UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(dto.getRequete());
      if (dto.getParams() != null && method == HttpMethod.GET) {
        dto.getParams().forEach(builder::queryParam);
      }

      HttpEntity<?> entity = new HttpEntity<>(headers);
      ResponseEntity<Object> response = restTemplate.exchange(
        builder.toUriString(),
        method,
        entity,
        Object.class
      );

      Map<String, Object> result = new HashMap<>();
      result.put("lienArray", response.getBody());
      result.put("headers", response.getHeaders());

      return ResponseEntity.ok(result);
    } catch (Exception ex) {
      Map<String, Object> error = new HashMap<>();
      error.put("error", Map.of(
        "status", 500,
        "message", ex.getMessage()
      ));
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
  }
}

