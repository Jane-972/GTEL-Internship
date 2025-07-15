package org.jane.gtelinternship.controllers;

import jakarta.validation.Valid;
import org.jane.gtelinternship.DTOs.UserInputDto;
import org.jane.gtelinternship.DTOs.UserOutputDto;
import org.jane.gtelinternship.models.UserModel;
import org.jane.gtelinternship.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserOutputDto createUser(@RequestBody @Valid UserInputDto userInputDTO) {

    UserModel savedUserModel = userService.createUser(userInputDTO.toModel());

    return UserOutputDto.fromModel(savedUserModel);
  }

  @GetMapping
  public List<UserOutputDto> getAllUsers() {
    List<UserModel> userModels = userService.getAllUsers();
    return userModels.stream()
      .map(UserOutputDto::fromModel)
      .collect(Collectors.toList());
  }

  @PatchMapping("/{id}")
  public UserModel updateUser(@PathVariable long id, @RequestBody UserModel userModel) {
    UserModel updatedUser = userService.updateUser(id, userModel);
    if (updatedUser == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
    return updatedUser;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable long id) {
    boolean isDeleted = userService.deleteUser(id);
    if (!isDeleted) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");
  }
}