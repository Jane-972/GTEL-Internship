package org.jane.gtelinternship.users.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jane.gtelinternship.users.domain.model.CreateUserModel;

public record UserInputDTO(
        @NotNull(message = "First name is required")
        @NotEmpty(message = "First name cannot be empty")
        String firstName,

        @NotNull(message = "Last name is required")
        @NotEmpty(message = "Last name cannot be empty")
        String lastName,

        @NotNull(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotNull(message = "Password is required")
        String password
) {
    public CreateUserModel toModel() {
        return new CreateUserModel(
                this.firstName,
                this.lastName,
                this.email,
                this.password
        );
    }
}
