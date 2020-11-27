package mx.com.inftel.wildfly.gradle_plugin

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

open class WildFlyUndeployTask : DefaultTask() {

    @get:Internal
    val controller: Property<String> = project.objects.property(String::class.java)

    @get:Internal
    val username: Property<String> = project.objects.property(String::class.java)

    @get:Internal
    val password: Property<String> = project.objects.property(String::class.java)

    @get:Internal
    val deploymentName: Property<String> = project.objects.property(String::class.java)

    init {
        group = groupName
        outputs.upToDateWhen { false }
    }

    @TaskAction
    fun execute() {
        commandContext(controller.get(), username.orNull, password.orNull) {
            if (isDeployed(deploymentName.get())) {
                undeploy(deploymentName.get())
            }
        }
    }
}