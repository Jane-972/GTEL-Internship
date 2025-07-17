package org.jane.gtelinternship.users.repo;

import org.jane.gtelinternship.IntegrationTestBase;
import org.jane.gtelinternship.users.domain.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepoIT extends IntegrationTestBase {
    @Autowired
    private UserRepo userRepo;

    @Test
    void testExistsByEmailAndFindByEmail() {
        UserEntity user = new UserEntity(
                UUID.randomUUID(),
                "test-first",
                "test-last",
                "test@example.com",
                "1234",
                UserRole.USER,
                false
        );

        UserEntity storedUser = userRepo.save(user);

        assertThat(storedUser).isEqualTo(user);
    }

    @Test
    void testFindByEmailReturnsNullForNonexistentEmail() {
        UserEntity found = userRepo.findByEmail("nonexistent@example.com");
        assertThat(found).isNull();
    }
}
