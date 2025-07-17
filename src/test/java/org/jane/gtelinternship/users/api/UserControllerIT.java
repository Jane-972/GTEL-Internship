package org.jane.gtelinternship.users.api;


import org.jane.gtelinternship.IntegrationTestBase;
import org.jane.gtelinternship.users.domain.model.UserRole;
import org.jane.gtelinternship.users.repo.UserEntity;
import org.jane.gtelinternship.users.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.json.JsonCompareMode;

import java.util.List;

import static org.jane.gtelinternship.TestObjects.UserIds.USER_ID_1;
import static org.jane.gtelinternship.TestObjects.UserIds.USER_ID_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerIT extends IntegrationTestBase {
    @Autowired
    UserRepo userRepo;

    @BeforeEach
    void setUp() {
        userRepo.deleteAll();
    }

    @Nested
    @DisplayName("When creating a new user")
    class UserCreationTest {
        @Test
        @DisplayName("Then the user should be created and approved if it's the first user")
        void shouldAddAndApproveFirstUser() throws Exception {
            Mockito.when(uuidGenerator.generateId()).thenReturn(USER_ID_1);

            mvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                {
                                    "firstName": "jane",
                                    "lastName": "doe",
                                    "email": "jane-doe@mail.com",
                                    "password": "1234",
                                    "role": "STUDENT"
                                }
                                """
                            )
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().json(
                            """
                                    {
                                        "id": "8a8ac60c-fbcc-4921-9349-a3105f570450",
                                        "firstName": "jane",
                                        "lastName": "doe",
                                        "email": "jane-doe@mail.com",
                                        "role" : "ADMIN"
                                    }
                                    """,
                            JsonCompareMode.STRICT
                    ))
            ;

            // Check DB content
            List<UserEntity> storedUsers = userRepo.findAll();
            assertEquals(1, storedUsers.size());
            assertEquals(
                    new UserEntity(USER_ID_1, "jane", "doe", "jane-doe@mail.com", "1234_encoded", UserRole.ADMIN, true),
                    storedUsers.getFirst()
            );
        }

        @Test
        @DisplayName("Then the user should be created but not approved if it's not the first user")
        void shouldNotApproveSecondUser() throws Exception {
            Mockito.when(uuidGenerator.generateId()).thenReturn(USER_ID_2);

            // Add first user
            userRepo.save(new UserEntity(USER_ID_1, "jane", "doe", "jane-doe@mail.com", "1234", UserRole.ADMIN, true));

            mvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                {
                                    "firstName": "Toni",
                                    "lastName": "Simmons",
                                    "email": "toni-simmons@mail.com",
                                    "password": "1234",
                                    "role": "STUDENT"
                                }
                                """
                            )
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().json(
                            """
                                    {
                                        "id": "4854a8d8-85d9-4fd1-a8e4-91d601f1a8dd",
                                        "firstName": "Toni",
                                        "lastName": "Simmons",
                                        "email": "toni-simmons@mail.com",
                                        "role" : "USER"
                                    }
                                    """,
                            JsonCompareMode.STRICT
                    ))
            ;

            // Check DB content
            UserEntity newUserEntity = userRepo.findById(USER_ID_2).orElseThrow();
            assertEquals(
                    new UserEntity(USER_ID_2, "Toni", "Simmons", "toni-simmons@mail.com", "1234_encoded", UserRole.USER, false),
                    newUserEntity
            );
        }
    }
}
