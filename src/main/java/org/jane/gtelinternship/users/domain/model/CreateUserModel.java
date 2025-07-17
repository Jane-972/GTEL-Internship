package org.jane.gtelinternship.users.domain.model;

public record CreateUserModel(
  String name,
  String email,
  String password
) {
}
