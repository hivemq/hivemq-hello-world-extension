plugins {
    id("com.hivemq.extension")
    id("com.github.hierynomus.license")
    id("com.github.sgtsilvio.gradle.utf8")
}

group = "com.hivemq.extensions"
description = "HiveMQ 4 Hello World Extension - a simple reference for all extension developers"

hivemqExtension {
    name = "Hello World Extension"
    author = "HiveMQ"
    priority = 1000
    startPriority = 1000
    mainClass = "$group.helloworld.HelloWorldMain"
    sdkVersion = "$version"
}

tasks.hivemqExtensionResources {
    from("LICENSE")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junit-jupiter.version")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("com.hivemq:hivemq-mqtt-client:${property("hivemq-mqtt-client.version")}")
    testImplementation("com.hivemq:hivemq-testcontainer-junit5:${property("hivemq-testcontainer.version")}")
}

tasks.test {
    useJUnitPlatform()
}

license {
    header = rootDir.resolve("HEADER")
    mapping("java", "SLASHSTAR_STYLE")
}

/* ******************** debugging ******************** */

val unzipHivemq by tasks.registering(Sync::class) {
    from(zipTree(rootDir.resolve("/your/path/to/hivemq-<VERSION>.zip")))
    into({ temporaryDir })
}

tasks.prepareHivemqHome {
    hivemqFolder.set(unzipHivemq.map { it.destinationDir.resolve("hivemq-<VERSION>") } as Any)
}

tasks.runHivemqWithExtension {
    debugOptions {
        enabled.set(false)
    }
}