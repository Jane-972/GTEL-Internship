package org.jane.gtelinternship.product.infra.client.woo.dto;

import org.jane.gtelinternship.product.domain.model.FullProduct;
import org.jane.gtelinternship.product.domain.model.ProductPrice;
import org.jane.gtelinternship.product.domain.model.WooProduct;

import java.util.Currency;

public record WooProductDto(
  Long id,
  String sku,
  String name,
  String shortDescription,
  Boolean inStock,
  Integer stockQuantity,
  Double price,
  Double regularPrice
) {
  private final static Currency dirham = Currency.getInstance("MAD");

  public FullProduct<WooProduct> toFullProduct() {
    return new FullProduct<>(
      new WooProduct(
        id,
        sku,
        name,
        shortDescription
      ),
      new ProductPrice(price, dirham),
      this.stockQuantity
    );
  }
}
