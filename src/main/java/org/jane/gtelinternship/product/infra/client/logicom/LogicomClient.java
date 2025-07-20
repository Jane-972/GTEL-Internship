package org.jane.gtelinternship.product.infra.client.logicom;

import org.jane.gtelinternship.common.exception.NotFoundException;
import org.jane.gtelinternship.product.infra.client.logicom.dto.LogicomInventoryDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

@Service
public class LogicomClient {
  private final RestClient logicomRestClient;
  private final LogicomClientConfig config;

  public LogicomClient(RestClient logicomRestClient, LogicomClientConfig config) {
    this.logicomRestClient = logicomRestClient;
    this.config = config;
  }

  public LogicomInventoryDto getProductInventory(List<String> skus) {
    String productIds = String.join(";", skus);
    long timestamp = System.currentTimeMillis() / 1000;
    String signature = generateSignature(timestamp);

    return logicomRestClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/api/GetInventory/")
        .queryParam("ProductID", productIds)
        .build())
      .header("Authorization", config.getAccessToken())
      .header("Timestamp", String.valueOf(timestamp))
      .header("Signature", signature)
      .header("CustomerID", String.valueOf(config.getCustomerId()))
      .retrieve()
      .body(LogicomInventoryDto.class);
  }

  public LogicomInventoryDto getProductInventory(String... skus) {
    return getProductInventory(List.of(skus));
  }

  private String generateSignature(long timestamp) {
    try {
      String data = config.getAccessToken() + timestamp;

      // Decode the access token key from Base64
      byte[] key = Base64.getDecoder().decode(config.getAccessTokenKey());

      // Create AES cipher
      SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

      // Encrypt and encode to Base64
      byte[] encrypted = cipher.doFinal(data.getBytes());
      return Base64.getEncoder().encodeToString(encrypted);

    } catch (Exception e) {
      throw new NotFoundException("Failed to generate signature" + e);
    }
  }
}