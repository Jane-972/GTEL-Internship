package org.jane.gtelinternship.users.api.dto;

import jakarta.annotation.Nullable;
import org.jane.gtelinternship.users.domain.model.PatchUserModel;

public record UserPatchDTO(
        @Nullable String firstName,
        @Nullable String lastName,
        @Nullable String email,
        @Nullable String password
) {
    public PatchUserModel toModel() {
        return new PatchUserModel(firstName, lastName, email, password);
    }
}
