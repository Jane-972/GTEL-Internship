package org.jane.gtelinternship.users.api.dto;



import org.jane.gtelinternship.users.domain.model.UserModel;
import org.jane.gtelinternship.users.domain.model.UserRole;

import java.util.UUID;

public record UserOutputDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        UserRole role
) {
    public static UserOutputDTO fromModel(UserModel userModel) {
        return new UserOutputDTO(
                userModel.id(),
                userModel.firstName(),
                userModel.lastName(),
                userModel.email(),
                userModel.role()
        );
    }
}
