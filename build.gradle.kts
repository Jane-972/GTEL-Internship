plugins {
    java
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.jane"
version = "0.0.1-SNAPSHOT"


repositories {
    mavenCentral()
}

dependencies {
    // Starter dependencies
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // DataBase
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.postgresql:postgresql")

    // Lombok
    val lombokVersion = "1.18.38"
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    // test/containers
    testImplementation("com.playtika.testcontainers:embedded-postgresql:3.1.13")
    testImplementation("org.springframework.cloud:spring-cloud-starter-bootstrap:4.2.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Docker
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
