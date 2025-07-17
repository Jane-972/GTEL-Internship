package org.jane.gtelinternship.users.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequestDto {

  private String name;

  @Email
  private String email;

  @Size(min = 6)
  private String password;

  private String image;
}
