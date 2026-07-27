plugins {
	java
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.teamtetra"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	//comment
	testImplementation ("io.rest-assured:rest-assured:6.0.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
	implementation("org.xerial:sqlite-jdbc:3.53.2.0")
	implementation("org.hibernate.orm:hibernate-community-dialects:8.0.0.Alpha1")
	implementation("org.springframework.security:spring-security-crypto")
	implementation("io.jsonwebtoken:jjwt-api:0.13.0")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")
	testImplementation("com.h2database:h2:2.4.240")
	// the core cucumber code
    testImplementation("io.cucumber:cucumber-java:7.33.0")
	// the integration code for cucumber & junit
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.33.0")
	// the sub module that gives us access to the junit test suite feature
    testImplementation("org.junit.platform:junit-platform-suite:1.14.1")
	// This gives access to the Selenium ecosystem
	implementation("org.seleniumhq.selenium:selenium-java:4.45.0")
	// This lets us inject our cucumber test resources into our step classes
	implementation("io.cucumber:cucumber-spring:7.34.4")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register("booting") {

    doLast {
        println("Houston, I think I'm bout to blow...")
		Thread.sleep(4000)
		println(
            """
                  \o/              \o/
                   |                |
                \  |  /          \  |  /

                      .------.
                    .'  o  o  '.
                   /   .----.   \
                  |   /      \   |
                  |  |  OOOO  |  |
                  |  |  OOOO  |  |
                   \  \______/  /
                    '.  ____  .'
                      '-.__.-'

                ;_;    AAAAAAAAAHHHHH    ;_;
            """.trimIndent()
        )
		Thread.sleep(1000)
		print("\u001b[H\u001b[2J\u001b[3J")
		System.out.flush()
    }
}

tasks.named("bootRun") {
    dependsOn("booting")
}
