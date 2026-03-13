plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.2"
    application
}

group = "net.makecoffee"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:26.1.0")
    implementation("net.minestom:minestom:2026.03.03-1.21.11")

    val logbackVersion = "1.5.25"
    implementation("ch.qos.logback:logback-core:$logbackVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation("com.google.guava:guava:33.5.0-jre")
    implementation("net.kyori:adventure-text-minimessage:4.26.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.mockito:mockito-core:5.23.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.23.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

private val appMainClass = "net.makecoffee.items.Main"

application {
    mainClass = appMainClass
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    jar {
        manifest {
            attributes["Main-Class"] = appMainClass
        }
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("")
    }

    test {
        useJUnitPlatform()
    }
}