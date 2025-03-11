
plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    java
}

group = "org.dismefront"
java.sourceCompatibility = JavaVersion.VERSION_18

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnit("4.13.2")
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(18)
    }
}
