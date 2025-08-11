package org.jane.gtelinternship.product.infra.client.logicom.mapper;

public class LogicomPriceUtil {
  static Double parsePrice(String priceStr) {
    try {
      return Double.parseDouble(priceStr.replace(",", ""));
    } catch (NumberFormatException e) {
      // No need to log for now
      return null;
    }
  }
}
