package org.jane.gtelinternship.models;

import java.time.LocalDateTime;

public record UserModel(
  Long id,
  String name,
  String email,
  String password,
  String passwordConfirmation,
  String image,
  boolean isActive,
  LocalDateTime emailVerifiedAt,
  String rememberToken
) {}
