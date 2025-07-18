package org.jane.gtelinternship;


import org.jane.gtelinternship.users.domain.model.UserRole;
import org.jane.gtelinternship.users.repo.UserEntity;

import java.util.Random;
import java.util.UUID;

import static org.jane.gtelinternship.TestObjects.UserIds.USER_ID_1;

public class TestObjects {
    public static String randomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + random.nextInt(rightLimit - leftLimit + 1);
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public static class UserIds {
        public static UUID USER_ID_1 = UUID.fromString("8a8ac60c-fbcc-4921-9349-a3105f570450");
        public static UUID USER_ID_2 = UUID.fromString("4854a8d8-85d9-4fd1-a8e4-91d601f1a8dd");
        public static UUID USER_ID_3 = UUID.fromString("f33a1b47-ea06-41e7-9933-35896efedc7a");
    }

    public static class UserEntities {
       public static UserEntity jane = new UserEntity(
                USER_ID_1,
                "Jane",
                "W",
                "jane@mail.com",
                "password123",
                UserRole.ADMIN,
                true
        );

        public static UserEntity generateUserEntity(UUID id, UserRole role) {
            return new UserEntity(id,
                    randomString(),
                    randomString(),
                    randomString() + "@mail.com",
                    randomString(),
                    role,
                    true
            );
        }
    }
}
