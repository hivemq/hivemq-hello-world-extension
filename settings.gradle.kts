rootProject.name = "hivemq-hello-world-extension"

pluginManagement {
    plugins {
        id("com.hivemq.extension") version "${extra["plugin.hivemq-extension.version"]}"
        id("io.github.sgtsilvio.gradle.defaults") version "${extra["plugin.defaults.version"]}"
        id("com.github.hierynomus.license") version "${extra["plugin.license.version"]}"
    }
}
