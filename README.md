# WildFly Deployment Gradle Plugin

Allows deploying and un-deploying to WildFly.

## Usage

1. Add to `build.gradle.kts`:

```
plugins {
    id("mx.com.inftel.wildfly") version "1.0.2"
}

wildfly {

    // Optional. Default value: "remote+http://localhost:9990".
    controller = "remote+http://myserver.mydomain.com:9990"

    // Optional. Default value: null (unset).
    var username = "user"

    // Optional. Default value: null (unset).
    var password = "password"

    // Required. Processed with "Project.file()" method.
    var deployment = "build/libs/webarchive.war"

    // Optional. Default value: true.
    var persistent = false
}
```

2. Deploy artifact:

```
./gradlew wildflyDeploy
```

3. Un-deploy artifact:

```
./gradlew wildflyUndeploy
```


### Exploded WAR

1. Add required plugins

```
plugins {
    war
    id("mx.com.inftel.wildfly") version "1.0.2"
}
```

2.  Configure plugin

```
wildfly {
    deployment = "build/libs/exploded/${project.name}.war"
    persistent = false
}
```

3. Register exploded war task

```
val explodedWar = tasks.register<Copy>("explodedWar") {
    group = "build"
    into("build/libs/exploded/${project.name}.war")
    with(tasks.war.get())
}
```

4. Fix dependency

```
tasks.wildflyDeploy.configure {
    dependsOn(explodedWar)
}
```
