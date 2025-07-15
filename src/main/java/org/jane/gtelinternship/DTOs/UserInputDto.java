package org.jane.gtelinternship.DTOs;

import org.jane.gtelinternship.models.CreateUserModel;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserInputDto(
  @NotNull(message = "Name is required")
  @NotEmpty(message = "Name cannot be empty")
  String name,

  @NotNull(message = "Email is required")
  @Email(message = "Email should be valid")
  String email,

  @NotNull(message = "Password is required")
  String password,

  @NotNull(message = "Password is required")
  String passwordConfirmation
) {
  public CreateUserModel toModel() {
    return new CreateUserModel(
      this.name,
      this.email,
      this.password,
      this.passwordConfirmation
    );
  }
}

