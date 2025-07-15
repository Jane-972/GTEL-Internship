-- Flyway Migration: Initial Schema

CREATE TABLE failed_jobs (
                             id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             uuid VARCHAR(255) NOT NULL UNIQUE,
                             connection TEXT NOT NULL,
                             queue TEXT NOT NULL,
                             payload LONGTEXT NOT NULL,
                             exception LONGTEXT NOT NULL,
                             failed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE migrations (
                            id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            migration VARCHAR(255) NOT NULL,
                            batch INT NOT NULL
);

CREATE TABLE password_resets (
                                 email VARCHAR(255) NOT NULL,
                                 token VARCHAR(255) NOT NULL,
                                 created_at TIMESTAMP NULL DEFAULT NULL,
                                 INDEX (email)
    );

CREATE TABLE personal_access_tokens (
                                        id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                        tokenable_type VARCHAR(255) NOT NULL,
                                        tokenable_id BIGINT UNSIGNED NOT NULL,
                                        name VARCHAR(255) NOT NULL,
                                        token VARCHAR(64) NOT NULL UNIQUE,
                                        abilities TEXT DEFAULT NULL,
                                        last_used_at TIMESTAMP NULL DEFAULT NULL,
                                        created_at TIMESTAMP NULL DEFAULT NULL,
                                        updated_at TIMESTAMP NULL DEFAULT NULL,
                                        INDEX idx_tokenable (tokenable_type, tokenable_id)
);

CREATE TABLE requetes (
                          id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          nom VARCHAR(255) DEFAULT NULL,
                          created_at TIMESTAMP NULL DEFAULT NULL,
                          updated_at TIMESTAMP NULL DEFAULT NULL
);

INSERT INTO requetes (id, nom, created_at, updated_at) VALUES
                                                           (1, 'get', NULL, NULL),
                                                           (2, 'post', NULL, NULL),
                                                           (3, 'put', NULL, NULL),
                                                           (4, 'patch', NULL, NULL),
                                                           (5, 'delete', NULL, NULL),
                                                           (6, 'head', NULL, NULL),
                                                           (7, 'option', NULL, NULL);

CREATE TABLE users (
                       id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       email_verified_at TIMESTAMP NULL DEFAULT NULL,
                       password VARCHAR(255) NOT NULL,
                       remember_token VARCHAR(100) DEFAULT NULL,
                       created_at TIMESTAMP NULL DEFAULT NULL,
                       updated_at TIMESTAMP NULL DEFAULT NULL,
                       image VARCHAR(255) DEFAULT 'profile.jpg',
                       is_active TINYINT(1) NOT NULL DEFAULT 1
);

