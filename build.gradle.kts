group = "mx.com.inftel.wildfly"
version = "1.0.1"

repositories {
    mavenCentral()
}

plugins {
    id("com.gradle.plugin-publish") version "0.14.0"
    `java-gradle-plugin`
    kotlin("jvm") version "1.4.31"
}

dependencies {
    // WildFly CLI
    compileOnly("org.wildfly.core:wildfly-cli:13.0.3.Final:client") {
        exclude(group = "*")
    }

    // Gradle
    compileOnly(gradleApi())

    // Kotlin
    implementation(kotlin("stdlib"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}

gradlePlugin {
    plugins {
        create("wildfly") {
            id = "mx.com.inftel.wildfly"
            implementationClass = "mx.com.inftel.wildfly.gradle_plugin.WildFlyPlugin"
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.jar.configure {
    val wildlfyCliJar = configurations.compileClasspath.get().files.first { it.name.startsWith("wildfly-cli-") }
    from(zipTree(wildlfyCliJar))
    duplicatesStrategy = DuplicatesStrategy.FAIL
}

pluginBundle {
    website = "https://github.com/santoszv/wildfly-gradle-plugin"
    vcsUrl = "https://github.com/santoszv/wildfly-gradle-plugin"
    description = "WildFly Deployment Gradle Plugin"
    tags = listOf("wildfly", "wildfly-cli", "jboss-cli")

    mavenCoordinates {
        groupId = project.group.toString()
        artifactId = project.name
        version = project.version.toString()
    }

    (plugins) {
        "wildfly" {
            displayName = "WildFly Deployment Gradle Plugin"
        }
    }
}