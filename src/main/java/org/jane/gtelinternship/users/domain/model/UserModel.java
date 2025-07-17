package org.jane.gtelinternship.users.domain.model;

import java.time.LocalDateTime;

public record UserModel(
  Long id,
  String name,
  String email,
  String password,
  String image,
  boolean isActive
) {}
