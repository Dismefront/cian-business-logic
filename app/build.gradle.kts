repositories {
    mavenLocal()
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":RosreestrRA"))
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("jakarta.platform:jakarta.jakartaee-api:11.0.0-RC1")
    implementation("org.wildfly:wildfly-client-all:36.0.1.Final")
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-webapp:7.23.0")
    implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-rest:7.23.0")
}