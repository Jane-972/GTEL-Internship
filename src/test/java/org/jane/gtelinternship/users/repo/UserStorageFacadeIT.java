package org.jane.gtelinternship.users.repo;

import org.jane.gtelinternship.IntegrationTestBase;
import org.jane.gtelinternship.TestObjects.UserEntities;
import org.jane.gtelinternship.common.exception.ConflictException;
import org.jane.gtelinternship.users.domain.model.CreateUserModel;
import org.jane.gtelinternship.users.domain.model.PatchUserModel;
import org.jane.gtelinternship.users.domain.model.UserModel;
import org.jane.gtelinternship.users.domain.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.jane.gtelinternship.TestObjects.UserIds.USER_ID_1;
import static org.jane.gtelinternship.TestObjects.UserIds.USER_ID_2;
import static org.junit.jupiter.api.Assertions.*;

class UserStorageFacadeIT extends IntegrationTestBase {
    private static final CreateUserModel createUserModel = new CreateUserModel("Jane", "Doe", "jane@example.com", "password");
    private static final UserModel janeUserModel = new UserModel(USER_ID_1, "Jane", "Doe", "jane@example.com", "password_encoded", true, UserRole.USER);
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserStorageFacade facade;

    @BeforeEach
    void setUp() {
        userRepo.deleteAll();
        Mockito.when(uuidGenerator.generateId()).thenReturn(USER_ID_1);
    }

    @Nested
    class IsFirstUserTests {
        @Test
        void shouldReturnTrueIfNoUsersExist() {
            userRepo.deleteAll();
            assertTrue(facade.isFirstUser());
        }

        @Test
        void shouldReturnFalseIfUsersExist() {
            userRepo.save(UserEntities.jane);
            assertFalse(facade.isFirstUser());
        }
    }

    @Nested
    class StoreUserTests {
        @Test
        void shouldStoreUserSuccessfully() {
            var user = facade.storeUser(createUserModel, UserRole.USER, true);

            assertEquals(janeUserModel, user);
        }

        @Test
        void shouldThrowConflictExceptionIfEmailExists() {
            facade.storeUser(createUserModel, UserRole.USER, true);

            // Attempt to store the same user again should throw ConflictException
            assertThrows(ConflictException.class, () -> facade.storeUser(createUserModel, UserRole.USER, true));
        }
    }

    @Nested
    class FetchUserByEmailTests {
        @Test
        void shouldReturnUserIfEmailExists() {
            facade.storeUser(createUserModel, UserRole.USER, true);

            UserModel user = facade.fetchUserByEmail("jane@example.com");
            assertEquals(janeUserModel, user);
        }

        @Test
        void shouldReturnNullIfEmailDoesNotExist() {
            assertNull(facade.fetchUserByEmail("notfound@example.com"));
        }
    }

    @Nested
    class FetchAllUsersTests {
        @Test
        void shouldReturnAllUsers() {
            facade.storeUser(new CreateUserModel("A", "B", "a@example.com", "pass"), UserRole.USER, true);

            Mockito.when(uuidGenerator.generateId()).thenReturn(USER_ID_2);
            facade.storeUser(new CreateUserModel("C", "D", "c@example.com", "pass"), UserRole.USER, true);

            assertEquals(2, facade.fetchAllUsers().size());
        }
    }

    @Nested
    class FetchUserByIdTests {
        @Test
        void shouldReturnUserIfIdExists() {
            UserModel user = facade.storeUser(createUserModel, UserRole.USER, true);
            assertEquals(janeUserModel, facade.fetchUserById(user.id()));
        }

        @Test
        void shouldReturnNullIfIdDoesNotExist() {
            assertNull(facade.fetchUserById(UUID.randomUUID()));
        }
    }

    @Nested
    class UpdateUserApprovalTests {
        @Test
        void shouldUpdateUserApproval() {
            UserModel user = facade.storeUser(new CreateUserModel("A", "B", "a@example.com", "pass"), UserRole.USER, false);
            UserModel updated = facade.updateUserApproval(user.id(), true);
            assertTrue(updated.approved());
        }
    }

    @Nested
    class UpdateUserProfileTests {
        @Test
        void shouldUpdateUserProfileFields() {
            UserModel user = facade.storeUser(new CreateUserModel("A", "B", "a@example.com", "pass"), UserRole.USER, true);
            PatchUserModel patch = new PatchUserModel("NewA", "NewB", "new@example.com", "newpass");
            UserModel updated = facade.updateUserProfile(user.id(), patch);
            assertEquals("NewA", updated.firstName());
            assertEquals("NewB", updated.lastName());
            assertEquals("new@example.com", updated.email());
        }
    }
}