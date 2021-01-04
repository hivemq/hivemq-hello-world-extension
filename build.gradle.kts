
plugins {
    id("com.hivemq.extension")
}

group = "com.hivemq.extensions"
description = "HiveMQ Hello World Extension"
version = "4.4.4"


hivemqExtension {
    name = "HiveMQ Hello World Extension"
    author = "HiveMQ"
    priority = 1000
    startPriority = 1000
    mainClass = "$group.HelloWorldMain"
    sdkVersion = "$version"
}

dependencies {
    testImplementation ("com.hivemq:hivemq-mqtt-client:1.2.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.1")
    testImplementation("com.hivemq:hivemq-testcontainer-junit5:1.1.1")
}

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
