plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.liverpool"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.sun.mail:jakarta.mail:2.0.1")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.mockito", module = "mockito-core")
	}
	testImplementation("org.mockito:mockito-core:4.8.0") // Última versión conocida
	testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
	testImplementation("org.assertj:assertj-core:3.24.2")
}


tasks.withType<Test> {
	useJUnitPlatform()
}
