package mx.com.inftel.wildfly.gradle_plugin

open class WildFlyExtension {

    /**
     * Optional parameter controller.
     *
     * Default value: `remote+http://localhost:9990`.
     */
    var controller: String? = null

    /**
     * Optional parameter username.
     *
     * Default value: `null`.
     */
    var username: String? = null

    /**
     * Optional parameter password.
     *
     * Default value: `null`.
     */
    var password: String? = null

    /**
     * Required parameter deployment. Every deployment (including
     * directories) must have an extension.
     *
     * This path is processed using `Project.file()` method.
     */
    var deployment: String? = null

    /**
     * Optional parameter persistent. Flag for persistent deployments across
     * server restarting.
     *
     * Default value: `true`.
     */
    var persistent: Boolean? = null

}