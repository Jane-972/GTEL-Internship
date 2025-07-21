package org.jane.gtelinternship.product.infra.client.logicom;

import org.jane.gtelinternship.common.exception.NotFoundException;
import org.springframework.http.HttpHeaders;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class LogicomHeadersUtil {
  public static HttpHeaders createLogicomHeaders(LogicomClientConfig config) {
    var requestTimeStamp = System.currentTimeMillis();

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", config.getAccessToken());
    headers.set("Timestamp", String.valueOf(requestTimeStamp));
    headers.set("Signature", generateSignature(config, requestTimeStamp));
    headers.set("CustomerID", String.valueOf(config.getCustomerId()));
    return headers;
  }

  private static String generateSignature(LogicomClientConfig config, long timestamp) {
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
      throw new NotFoundException("Failed to generate signature: " + e.getMessage());
    }
  }
}
