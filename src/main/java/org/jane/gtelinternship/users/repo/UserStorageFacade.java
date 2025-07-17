package org.jane.gtelinternship.users.repo;

import io.micrometer.common.lang.Nullable;
import jakarta.transaction.Transactional;
import org.jane.gtelinternship.common.exception.ConflictException;
import org.jane.gtelinternship.common.service.UuidGenerator;
import org.jane.gtelinternship.users.domain.model.CreateUserModel;
import org.jane.gtelinternship.users.domain.model.PatchUserModel;
import org.jane.gtelinternship.users.domain.model.UserModel;
import org.jane.gtelinternship.users.domain.model.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserStorageFacade {
    private final UserRepo userRepo;
    private final UuidGenerator userIdGenerator;
    private final PasswordEncoder userPasswordEncoder;

    public UserStorageFacade(
            UserRepo userRepo,
            UuidGenerator userIdGenerator,
            PasswordEncoder userPasswordEncoder
    ) {
        this.userRepo = userRepo;
        this.userIdGenerator = userIdGenerator;
        this.userPasswordEncoder = userPasswordEncoder;
    }

    public boolean isFirstUser() {
        return userRepo.count() == 0;
    }

    @Transactional
    public UserModel storeUser(CreateUserModel userModel, UserRole role, boolean isApproved) {
        if (!userRepo.existsByEmail(userModel.email())) {
            UserEntity userEntity = new UserEntity(
                    userIdGenerator.generateId(),
                    userModel.firstName(),
                    userModel.lastName(),
                    userModel.email(),
                    userPasswordEncoder.encode(userModel.password()),
                    role,
                    isApproved
            );

            return userRepo.save(userEntity).toModel();
        } else {
            throw new ConflictException("A user with email: `" + userModel.email() + "` already exists");
        }
    }

    @Nullable
    public UserModel fetchUserByEmail(String email) {
        UserEntity userEntity = userRepo.findByEmail(email);
        return userEntity != null ? userEntity.toModel() : null;
    }

    public List<UserModel> fetchAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(UserEntity::toModel)
                .toList();
    }

    @Nullable
    public UserModel fetchUserById(UUID userId) {
        return userRepo.findById(userId)
                .map(UserEntity::toModel)
                .orElse(null);
    }

    @Transactional
    public UserModel updateUserApproval(UUID userId, boolean approved) {
        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userEntity.setApproved(approved);
        return userRepo.save(userEntity).toModel();
    }

    @Transactional
    public UserModel updateUserProfile(UUID userId, PatchUserModel updateRequest) {
        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updateRequest.firstName() != null) {
            userEntity.setFirstName(updateRequest.firstName());
        }
        if (updateRequest.lastName() != null) {
            userEntity.setLastName(updateRequest.lastName());
        }
        if (updateRequest.email() != null) {
            userEntity.setEmail(updateRequest.email());
        }
        if (updateRequest.password() != null) {
            userEntity.setPassword(userPasswordEncoder.encode(updateRequest.password()));
        }

        return userRepo.save(userEntity).toModel();
    }

}
