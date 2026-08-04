plugins {
    alias(libs.plugins.hivemq.extension)
    alias(libs.plugins.defaults)
    alias(libs.plugins.spotless)
}

group = "com.hivemq.extensions"
description = "HiveMQ 4 Hello World Extension - a simple reference for all extension developers"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.compileJava {
    javaCompiler = javaToolchains.compilerFor {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

hivemqExtension {
    name = "Hello World Extension"
    author = "HiveMQ"
    priority = 1000
    startPriority = 1000
    mainClass = "$group.helloworld.HelloWorldMain"
    sdkVersion = "$version"

    resources {
        from("LICENSE")
    }
}

val mockitoAgent = configurations.create("mockitoAgent") {
    isCanBeConsumed = false
    isCanBeResolved = true
}
dependencies {
    mockitoAgent(libs.mockito) { isTransitive = false }
}
class MockitoAgentArgumentProvider(@get:Classpath val agentJar: FileCollection) : CommandLineArgumentProvider {
    override fun asArguments(): Iterable<String> = listOf("-javaagent:${agentJar.singleFile}")
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        withType<JvmTestSuite> {
            useJUnitJupiter(libs.versions.junit.jupiter)
            targets.configureEach {
                testTask {
                    jvmArgumentProviders.add(MockitoAgentArgumentProvider(mockitoAgent))
                    // see https://netty.io/wiki/java-24-and-sun.misc.unsafe.html
                    jvmArgs("--enable-native-access=ALL-UNNAMED", "--sun-misc-unsafe-memory-access=allow")
                }
            }
        }
        "test"(JvmTestSuite::class) {
            dependencies {
                implementation(libs.mockito)
            }
        }
        "integrationTest"(JvmTestSuite::class) {
            dependencies {
                compileOnly(libs.jetbrains.annotations)
                implementation(libs.hivemq.mqttClient)
                implementation(libs.testcontainers.junitJupiter)
                implementation(libs.testcontainers.hivemq)
                runtimeOnly(libs.logback.classic)
            }
        }
    }
}

spotless {
    java {
        licenseHeaderFile(rootDir.resolve("HEADER"))
    }
}

/* ******************** debugging ******************** */

tasks.prepareHivemqHome {
    hivemqHomeDirectory = file("/your/path/to/hivemq-<VERSION>")
}

tasks.runHivemqWithExtension {
    debugOptions {
        enabled = false
    }
}
