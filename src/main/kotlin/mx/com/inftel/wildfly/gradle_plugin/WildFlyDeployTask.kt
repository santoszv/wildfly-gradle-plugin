package mx.com.inftel.wildfly.gradle_plugin

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

open class WildFlyDeployTask : DefaultTask() {

    @get:Internal
    val controller: Property<String> = project.objects.property(String::class.java)

    @get:Internal
    val username: Property<String> = project.objects.property(String::class.java)

    @get:Internal
    val password: Property<String> = project.objects.property(String::class.java)

    @get:Internal
    val deploymentName: Property<String> = project.objects.property(String::class.java)

    @get:Internal
    val deploymentPath: Property<String> = project.objects.property(String::class.java)

    @get:Internal
    val deploymentArchive: Property<Boolean> = project.objects.property(Boolean::class.java)

    @get:Internal
    val deploymentPersistent: Property<Boolean> = project.objects.property(Boolean::class.java)

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
            deploy(deploymentName.get(), deploymentPath.get(), deploymentArchive.get(), deploymentPersistent.get())
        }
    }
}