@file:Suppress("unused", "DuplicatedCode")

package mx.com.inftel.wildfly.gradle_plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

open class WildFlyPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        // Extension
        val extension = target.extensions.create(extensionName, WildFlyExtension::class.java)
        // Parameters
        val controller: String by lazy {
            extension.controller ?: parameterControllerDefault
        }
        val username: String? by lazy {
            extension.username
        }
        val password: String? by lazy {
            extension.password
        }
        val deployment: File by lazy {
            target.file(extension.deployment ?: throw WildFlyException(messageDeploymentIsNotSet))
        }
        val persistent: Boolean by lazy {
            extension.persistent ?: parameterPersistentDefault
        }
        // Deploy task
        target.tasks.register(taskDeployName, WildFlyDeployTask::class.java).configure { task ->
            task.controller.set(target.provider {
                controller
            })
            task.username.set(target.provider {
                username
            })
            task.password.set(target.provider {
                password
            })
            task.deploymentName.set(target.provider {
                deployment.name
            })
            task.deploymentPath.set(target.provider {
                deployment.absolutePath
            })
            task.deploymentArchive.set(target.provider {
                when {
                    deployment.isFile -> true
                    deployment.isDirectory -> false
                    else -> throw WildFlyException(messageDeploymentIsNotValid)
                }
            })
            task.deploymentPersistent.set(target.provider {
                persistent
            })
        }
        // Undeploy task
        target.tasks.register(taskUndeployName, WildFlyUndeployTask::class.java).configure { task ->
            task.controller.set(target.provider {
                controller
            })
            task.username.set(target.provider {
                username
            })
            task.password.set(target.provider {
                password
            })
            task.deploymentName.set(target.provider {
                deployment.name
            })
        }
    }
}