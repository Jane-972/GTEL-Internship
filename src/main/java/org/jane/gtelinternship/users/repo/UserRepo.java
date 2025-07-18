package org.jane.gtelinternship.users.repo;

import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface UserRepo extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmail(String email);

    @Nullable
    UserEntity findByEmail(String email);
}
