package mx.com.inftel.wildfly.gradle_plugin

import org.gradle.api.GradleException

class WildFlyException(message: String, cause: Throwable? = null) : GradleException(message, cause)