package org.jane.gtelinternship.product.domain.model;

import lombok.NonNull;

public record FullProduct(
  Product product,
  @NonNull
  ProductPrice price,
  int availableStock
){

}
