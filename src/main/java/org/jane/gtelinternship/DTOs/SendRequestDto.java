package org.jane.gtelinternship.DTOs;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class SendRequestDto {
  private String requete;
  private Integer typeRequete;
  private List<HeaderDto> headers;
  private Map<String, String> params;

  @Data
  public static class HeaderDto {
    private String key;
    private String value;
  }
}
