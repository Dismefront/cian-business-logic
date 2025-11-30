plugins {
    id("java")
    id("distribution")
}

dependencies {
    implementation("jakarta.resource:jakarta.resource-api:2.1.0")
}

tasks.register<Zip>("rar") {
    group = "build"
    description = "Builds a Resource Adapter Archive (.rar)"

    archiveBaseName.set("rr-adapter")
    archiveExtension.set("rar") // important

    from(sourceSets.main.get().output)

    from("src/main/resources/META-INF") {
        into("META-INF") // include ra.xml etc.
        exclude("MANIFEST.MF")
        exclude("ra.xml")
    }
}