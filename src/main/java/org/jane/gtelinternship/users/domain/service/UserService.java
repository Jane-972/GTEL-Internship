package org.jane.gtelinternship.users.domain.service;

import org.jane.gtelinternship.users.domain.model.CreateUserModel;
import org.jane.gtelinternship.users.domain.model.UserModel;
import org.jane.gtelinternship.users.repo.UserEntity;
import org.jane.gtelinternship.users.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class UserService {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public Stream<UserModel> getAllUsers() {
    return userRepo.findAll().stream()
      .map(UserEntity::toModel);
  }


  public UserModel createUser(CreateUserModel userModel) {
    String hashedPassword = passwordEncoder.encode(userModel.password());
    UserEntity entity = new UserEntity(
      null,
      userModel.name(),
      userModel.email(),
      hashedPassword,
      "profile.jpg",
      true
    );

    return userRepo.save(entity).toModel();
  }


  public UserModel updateUser(long id, UserModel updatedModel) {
    return userRepo.findById(id)
      .map(userEntity -> {
        if (updatedModel.name() != null) {
          userEntity.setName(updatedModel.name());
        }
        if (updatedModel.email() != null) {
          userEntity.setEmail(updatedModel.email());
        }
        if (updatedModel.password() != null) {
          userEntity.setPassword(updatedModel.password());
        }
        UserEntity updatedEntity = userRepo.save(userEntity);
        return updatedEntity.toModel();
      })
      .orElse(null); // return null if not found
  }

  public boolean deleteUser(long id) {
    return userRepo.findById(id)
      .map(employeeEntity -> {
        userRepo.delete(employeeEntity);
        return true;
      })
      .orElse(false);
  }
}
