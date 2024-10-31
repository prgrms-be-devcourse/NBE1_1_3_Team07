plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
	kotlin("jvm") version "2.0.21"
	id("org.jetbrains.kotlin.plugin.spring") version "1.8.10" // all-open, no-arg 플러그인
	id("org.jetbrains.kotlin.plugin.jpa") version "1.9.25"
	id("org.jetbrains.kotlin.kapt") version "2.0.21"
}

group = "com.develetter"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

repositories {
	mavenCentral()
}

val snippetsDir = file("build/generated-snippets")

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-batch")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.core:jackson-databind")
	implementation("org.json:json:20231013")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
	implementation("io.github.cdimascio:dotenv-java:2.2.0")
	implementation("io.netty:netty-resolver-dns-native-macos:4.1.104.Final:osx-x86_64")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

	runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.104.Final:osx-aarch_64")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.mysql:mysql-connector-j")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.batch:spring-batch-test")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// QueryDsl with KAPT
	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
	kapt("jakarta.annotation:jakarta.annotation-api")
	kapt("jakarta.persistence:jakarta.persistence-api")

	// Kotlin and Jackson
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.+")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.11.2")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.2")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.2")

	// Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

	// Redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis")

	// Additional Kotlin logging library
	implementation("io.github.microutils:kotlin-logging:3.0.0")
}

tasks.named<Test>("test") {
	outputs.dir(snippetsDir)
	useJUnitPlatform()
}

tasks.named("asciidoctor") {
	inputs.dir(snippetsDir)
	dependsOn(tasks.named("test"))
}

configurations {
	named("compileOnly") {
		extendsFrom(configurations.annotationProcessor.get())
	}
}
