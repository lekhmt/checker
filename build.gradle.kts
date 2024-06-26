plugins {
	java
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
	id("com.google.devtools.ksp") version "1.9.21-1.0.15"
	id("org.jetbrains.kotlin.plugin.spring") version "1.9.23"
	kotlin("jvm")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

val jimmerVersion = "0.8.126"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.flywaydb:flyway-core")
	implementation("com.h2database:h2:2.2.224")
	implementation("org.springdoc:springdoc-openapi-starter-common:2.5.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.2.5")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	implementation("commons-io:commons-io:2.6")
	implementation("net.lingala.zip4j:zip4j:2.11.5")


	implementation("org.babyfish.jimmer:jimmer-spring-boot-starter:${jimmerVersion}")
	implementation("org.babyfish.jimmer:jimmer-core-kotlin:${jimmerVersion}")
	ksp("org.babyfish.jimmer:jimmer-ksp:${jimmerVersion}")
}

tasks.withType<Test> {
	useJUnitPlatform()
}