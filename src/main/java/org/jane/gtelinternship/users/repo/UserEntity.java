package org.jane.gtelinternship.users.repo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jane.gtelinternship.users.domain.model.UserModel;
import org.jane.gtelinternship.users.domain.model.UserRole;

import java.util.UUID;


@Getter
@Setter
@Entity(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserEntity {
  @Id
  protected UUID id;
  protected String firstName;
  protected String lastName;
  protected String email;
  protected String password;
  protected boolean approved;
  @Enumerated(EnumType.STRING)
  protected UserRole role;

  public UserEntity() {
  }

  public UserEntity(UUID id, String firstName, String lastName, String email, String password, UserRole role, Boolean isApproved) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.role = role;
    this.approved = isApproved;
  }

  public UserModel toModel() {
    return new UserModel(
            id,
            firstName,
            lastName,
            email,
            password,
            approved,
            role
    );
  }
}
