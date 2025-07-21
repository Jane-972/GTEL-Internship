package org.jane.gtelinternship.common.encrypt;

import org.jane.gtelinternship.common.exception.NotFoundException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {
  /**
   * Encrypts the given data using AES encryption with the provided key.
   *
   * @param key  The Base64 encoded AES key.
   * @param data The data to encrypt.
   * @return The encrypted data as a Base64 encoded string.
   */
  public static String encrypt(String key, String data) {
    try {
      // Decode the access token key from Base64
      byte[] decodedKey = Base64.getDecoder().decode(key);

      // Create AES cipher
      SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

      // Encrypt and encode to Base64
      byte[] encrypted = cipher.doFinal(data.getBytes());
      return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      throw new NotFoundException("Failed to encrypt data: " + e.getMessage());
    }
  }
}
