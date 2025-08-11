package org.jane.gtelinternship.product.domain.model;

import lombok.NonNull;

public record  FullProduct<T extends Product>(
  T product,
  @NonNull
  ProductPrice price,
  int availableStock
){
}
