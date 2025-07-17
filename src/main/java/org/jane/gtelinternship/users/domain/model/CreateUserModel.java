package org.jane.gtelinternship.users.domain.model;

public record CreateUserModel(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
