package org.jane.gtelinternship.users.repo;

import jakarta.persistence.*;
import org.jane.gtelinternship.users.domain.model.UserModel;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String image = "profile.jpg";

  @Column(nullable = false)
  private boolean isActive = true;

  private LocalDateTime emailVerifiedAt;

  private String rememberToken;

  public UserEntity() {
  }

  public UserEntity(Long id, String name, String email, String password,
                    String image, boolean isActive, LocalDateTime emailVerifiedAt, String rememberToken) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.image = image;
    this.isActive = isActive;
    this.emailVerifiedAt = emailVerifiedAt;
    this.rememberToken = rememberToken;
  }

  public UserModel toModel() {
    return new UserModel(
      id,
      name,
      email,
      password,
      image,
      isActive,
      emailVerifiedAt,
      rememberToken
    );
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setName(String name) {
    this.name = name;
  }


}
