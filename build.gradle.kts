plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    java
}

subprojects {

    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "java")

    group = "org.dismefront"
    java.sourceCompatibility = JavaVersion.VERSION_18

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.projectlombok:lombok")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-security")
        annotationProcessor("org.projectlombok:lombok")
        implementation("org.postgresql:postgresql")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        implementation("com.atomikos:transactions-jta:6.0.0")
        implementation("com.atomikos:transactions-jdbc:6.0.0")
        implementation("org.springframework.boot:spring-boot-starter-jta-atomikos:2.7.18")
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
}