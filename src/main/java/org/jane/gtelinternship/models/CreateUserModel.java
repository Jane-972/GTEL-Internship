package org.jane.gtelinternship.models;

public record CreateUserModel(
  String name,
  String email,
  String password
) {
}
