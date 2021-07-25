group = "mx.com.inftel.wildfly"
version = "1.0.2"

repositories {
    mavenCentral()
}

plugins {
    id("com.gradle.plugin-publish") version "0.15.0"
    `java-gradle-plugin`
    kotlin("jvm") version "1.4.31"
}

dependencies {

    // WildFly CLI
    compileOnly("org.wildfly.core:wildfly-cli:16.0.1.Final:client") {
        exclude(group = "org.aesh", module = "readline")
        exclude(group = "org.aesh", module = "aesh")
        exclude(group = "org.aesh", module = "aesh-extensions")
        exclude(group = "org.jboss", module = "staxmapper")
        exclude(group = "org.jboss.modules", module = "jboss-modules")
        exclude(group = "org.wildfly.common", module = "wildfly-common")
        exclude(group = "org.wildfly.core", module = "wildfly-controller-client")
        exclude(group = "org.wildfly.core", module = "wildfly-embedded")
        exclude(group = "org.wildfly.security", module = "wildfly-elytron-auth")
        exclude(group = "org.wildfly.security", module = "wildfly-elytron-base")
        exclude(group = "org.wildfly.security", module = "wildfly-elytron-credential")
        exclude(group = "org.wildfly.security", module = "wildfly-elytron-password-impl")
        exclude(group = "org.wildfly.security", module = "wildfly-elytron-security-manager")
        exclude(group = "org.wildfly.security", module = "wildfly-elytron-security-manager-action")
        exclude(group = "org.wildfly.security", module = "wildfly-elytron-x500-cert-acme")
        exclude(group = "org.jboss.logging", module = "jboss-logging")
        exclude(group = "org.jboss.logging", module = "jboss-logging-annotations")
        exclude(group = "org.jboss.logging", module = "jboss-logging-processor")
        exclude(group = "org.jboss.logmanager", module = "jboss-logmanager")
        exclude(group = "org.jboss.remoting", module = "jboss-remoting")
        exclude(group = "org.jboss.remotingjmx", module = "remoting-jmx")
        exclude(group = "org.jboss", module = "jboss-vfs")
        exclude(group = "org.jboss.stdio", module = "jboss-stdio")
        exclude(group = "org.glassfish", module = "jakarta.json")
        exclude(group = "org.picketbox", module = "picketbox")
        exclude(group = "com.fasterxml.woodstox", module = "woodstox-core")
        exclude(group = "xerces", module = "xercesImpl")
        exclude(group = "junit", module = "junit")
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
    val wildflyCliJar = configurations.compileClasspath.get().files.first { it.name.startsWith("wildfly-cli-") }
    from(zipTree(wildflyCliJar))
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