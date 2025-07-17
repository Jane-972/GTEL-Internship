package org.jane.gtelinternship.users.api;

import jakarta.validation.Valid;
import org.jane.gtelinternship.users.api.dto.UserInputDto;
import org.jane.gtelinternship.users.api.dto.UserOutputDto;
import org.jane.gtelinternship.users.domain.model.UserModel;
import org.jane.gtelinternship.users.domain.service.UserService;
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
    return userService.getAllUsers()
      .map(UserOutputDto::fromModel)
      .toList();
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