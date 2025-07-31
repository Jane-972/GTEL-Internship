package org.jane.gtelinternship.common.encrypt;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.spec.IvParameterSpec;

public class EncryptionUtil {
  /**
   * Encrypts the given data using AES CBC mode with a static IV of 16 null bytes.
   *
   * @param key  The Base64 encoded AES key.
   * @param data The data to encrypt.
   * @return The encrypted data as a Base64 encoded string.
   */
  public static String encrypt(String key, String data) {
    try {
      // Decode the access token key from Base64
      byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

      // Create the AES key spec
      SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

      // Set IV to 16 null bytes
      IvParameterSpec ivSpec = new IvParameterSpec(new byte[16]); // 16 null bytes

      // Create AES cipher in CBC mode with PKCS5 padding
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

      // Encrypt and encode to Base64
      byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
      byte[] encrypted = cipher.doFinal(dataBytes);
      return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      throw new RuntimeException("Encryption failed", e);
    }
  }
}
