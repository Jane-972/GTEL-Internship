package org.jane.gtelinternship.stockupdater.api.dto.logicom;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LogicomResponseDto(
  @JsonProperty("StatusCode") int statusCode,
  @JsonProperty("Status") String status,
  @JsonProperty("Message") List<LogicomProductDto> message,
  @JsonProperty("NextItemNo") String nextItemNo
) {}
