import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.14.0"

    kotlin("jvm") version "2.2.0"
    kotlin("plugin.spring") version "2.2.0"
}

group = "org.jane"
version = "0.0.1-SNAPSHOT"


repositories {
    mavenCentral()
}

sourceSets {
    main {
        java {
            srcDir("$buildDir/generated-api/logicom/src/main/java")
        }
    }

    test {
        java {
            srcDir("$buildDir/generated-api/logicom/src/main/java")
        }
    }
}

dependencies {
    // Starter dependencies
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")


    // DataBase
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.postgresql:postgresql")

    // Spring doc
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")

    //Payment
    implementation("com.stripe:stripe-java:29.1.0")

    // Lombok
    val lombokVersion = "1.18.38"
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    // test/containers
    testImplementation("com.playtika.testcontainers:embedded-postgresql:3.1.15")
    testImplementation("org.springframework.cloud:spring-cloud-starter-bootstrap:4.2.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")


    // Docker
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.1")
    }
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    withType<JavaCompile> {
        dependsOn(openApiGenerate)
    }

    withType<KotlinCompile> {
        dependsOn(openApiGenerate)
    }
}

openApiGenerate {
    generatorName.set("java")
    inputSpec.set("$rootDir/openapi/logicom/logicomApi.yaml")
    outputDir.set("$buildDir/generated-api/logicom")
    modelPackage.set("org.jane.gtelinternship.logicom")
    modelNameSuffix.set("Dto")

    library.set("restclient")

    configOptions.set(
        mapOf(
            "dateLibrary" to "java8",
            "openApiNullable" to "false",
            "useJakartaEe" to "true"
        )
    )
    globalProperties.set(
        mapOf(
            "models" to "",
            "apis" to "false",
            "supportingFiles" to "false",
            "modelDocs" to "false",
            "generateApiTests" to "false",
            "generateModelTests" to "false",
            "generateApiDocumentation" to "false",
            "generateModelDocumentation" to "false"
        )
    )
}