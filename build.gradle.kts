group = "mx.com.inftel.wildfly"
version = "1.0.0"

repositories {
    mavenCentral()
}

plugins {
    id("com.gradle.plugin-publish") version "0.14.0"
    `java-gradle-plugin`
    kotlin("jvm") version "1.3.72"
}

dependencies {
    // WildFly CLI
    implementation("org.wildfly.core:wildfly-cli:13.0.3.Final:client") {
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

pluginBundle {
    website = "https://github.com/santoszv/wildfly-gradle-plugin"
    vcsUrl = "https://github.com/santoszv/wildfly-gradle-plugin"
    description = "WildFly Deployment Gradle Plugin"
    tags = listOf("wildfly", "wildfly-cli", "jboss-cli")

    (plugins) {
        "wildfly" {
            displayName = "WildFly Deployment Gradle Plugin"
        }
    }
}