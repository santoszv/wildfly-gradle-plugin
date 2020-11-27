@file:Suppress("unused")

package mx.com.inftel.wildfly.gradle_plugin

import org.jboss.`as`.cli.CommandContext
import org.jboss.`as`.cli.CommandContextFactory
import org.jboss.`as`.cli.impl.CommandContextConfiguration
import org.jboss.dmr.ModelNode

const val extensionName = "wildfly"

const val groupName = "wildfly"

const val messageDeploymentIsNotSet = "WildFly deployment is not set"
const val messageDeploymentIsNotValid = "WildFly deployment is not valid"

const val parameterControllerDefault = "remote+http://localhost:9990"
const val parameterPersistentDefault = true

const val taskDeployName = "wildflyDeploy"
const val taskUndeployName = "wildflyUndeploy"

inline fun commandContext(controller: String, username: String?, password: String?, block: CommandContext.() -> Unit) {
    val configBuilder = CommandContextConfiguration.Builder()
    configBuilder.setInitConsole(false)
    configBuilder.setController(controller)
    if (username != null) {
        configBuilder.setUsername(username)
    }
    if (password != null) {
        configBuilder.setPassword(password.toCharArray())
    }
    val ctxFactory = CommandContextFactory.getInstance()
    val commandContext = ctxFactory.newCommandContext(configBuilder.build())
    commandContext.connectController(controller)
    try {
        commandContext.block()
    } finally {
        commandContext.disconnectController()
    }
}

fun CommandContext.isDeployed(deployment: String): Boolean {
    val request = ModelNode().apply {
        get("operation").set("read-children-resources")
        get("child-type").set("deployment")
        get("include-runtime").set(true)
    }
    val response = modelControllerClient.execute(request)
    response.checkOutcome()
    val result = response.get("result")
    return result.keys().contains(deployment)
}

fun CommandContext.undeploy(deployment: String) {
    val request = ModelNode().apply {
        get("operation").set("composite")
        get("address").setEmptyList()
        get("steps").apply {
            add().apply {
                get("operation").set("undeploy")
                get("address").apply {
                    add().apply {
                        set("deployment", deployment)
                    }
                }
            }
            add().apply {
                get("operation").set("remove")
                get("address").apply {
                    add().apply {
                        set("deployment", deployment)
                    }
                }
            }
        }
    }
    val response = modelControllerClient.execute(request)
    response.checkOutcome()
}

fun CommandContext.deploy(deployment: String, path: String, archive: Boolean, persistent: Boolean) {
    val request = ModelNode().apply {
        get("operation").set("composite")
        get("address").setEmptyList()
        get("steps").apply {
            add().apply {
                get("operation").set("add")
                get("address").apply {
                    add().apply {
                        set("deployment", deployment)
                    }
                }
                get("content").get(0).apply {
                    get("path").set(path)
                    get("archive").set(archive)
                }
                get("persistent").set(persistent)
            }
            add().apply {
                get("operation").set("deploy")
                get("address").apply {
                    add().apply {
                        set("deployment", deployment)
                    }
                }
            }
        }
    }
    val response = modelControllerClient.execute(request)
    response.checkOutcome()
}

fun ModelNode.checkOutcome() {
    when (get("outcome").asString()) {
        "success" -> Unit
        "failed" -> throw WildFlyException(get("failure-description").toString())
        else -> throw WildFlyException(toString())
    }
}