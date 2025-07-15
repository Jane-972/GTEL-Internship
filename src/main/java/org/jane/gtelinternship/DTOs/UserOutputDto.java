package org.jane.gtelinternship.DTOs;


import org.jane.gtelinternship.models.UserModel;


public record UserOutputDto(
  Long id,
  String name,
  String email,
  String image
) {
  public static UserOutputDto fromModel(UserModel userModel) {
    return new UserOutputDto(
      userModel.id(),
      userModel.name(),
      userModel.email(),
      userModel.image()
    );
  }
}
