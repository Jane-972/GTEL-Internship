package org.jane.gtelinternship.services;

import org.jane.gtelinternship.models.CreateUserModel;
import org.jane.gtelinternship.models.UserModel;
import org.jane.gtelinternship.repos.UserEntity;
import org.jane.gtelinternship.repos.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

  private final UserRepo userRepo;

  public UserService(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  public List<UserModel> getAllUsers() {
    return userRepo.findAll().stream()
      .map(UserEntity::toModel)
      .toList();
  }

  public UserModel createUser(CreateUserModel userModel) {
    UserEntity entity = new UserEntity(
      null,
      userModel.name(),
      userModel.email(),
      userModel.password(),
      userModel.passwordConfirmation(),
      "profile.jpg",
      true,
      null,
      null
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
