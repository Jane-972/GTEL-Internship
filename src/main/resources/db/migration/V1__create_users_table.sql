CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       email_verified_at TIMESTAMP NULL,
                       password VARCHAR(255) NOT NULL,
                       remember_token VARCHAR(100),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       image VARCHAR(255) DEFAULT 'profile.jpg',
                       is_active BOOLEAN NOT NULL DEFAULT TRUE
);


