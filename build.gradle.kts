plugins {
    id("io.micronaut.application") version "3.4.0"
    id("io.micronaut.aot") version "3.4.0"
    id("com.google.cloud.tools.jib") version "3.2.1"
    id("com.github.ben-manes.versions") version "0.42.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

version = "0.1"
group = "com.photomath.roundtable"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("io.dekorate:openshift-annotations")
    annotationProcessor("io.dekorate:prometheus-annotations")
    annotationProcessor("io.micronaut.openapi:micronaut-openapi")
    annotationProcessor("io.micronaut:micronaut-http-validation")
    compileOnly("org.graalvm.nativeimage:svm")
    implementation("ch.qos.logback:logback-classic")
    implementation("io.dekorate:openshift-annotations")
    implementation("io.dekorate:prometheus-annotations")
    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")
    implementation("io.micronaut.reactor:micronaut-reactor")
    implementation("io.micronaut.reactor:micronaut-reactor-http-client")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.swagger.core.v3:swagger-annotations")
    implementation("jakarta.annotation:jakarta.annotation-api")

    if (System.getProperty("os.name") == "Mac OS X") {
        val qualifier = if (System.getProperty("os.arch") == "aarch64") "osx-aarch_64" else "osx-x86_64"
        runtimeOnly("io.netty:netty-transport-native-kqueue:4.1.76.Final:$qualifier")
        runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.76.Final:$qualifier")
    } else {
        runtimeOnly("io.netty:netty-transport-native-epoll:4.1.76.Final:linux-x86_64")
    }
}

application {
    mainClass.set("com.photomath.roundtable.Application")
}

java {
    sourceCompatibility = JavaVersion.toVersion("17")
    targetCompatibility = JavaVersion.toVersion("17")
}

tasks {
    jib {
        to {
            image = "gcr.io/jak-1/jak-micronaut"
        }
    }
}

graalvmNative.toolchainDetection.set(false)

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.photomath.roundtable.*")
    }
    version("3.4.3")
    aot {
        optimizeServiceLoading.set(true)
        convertYamlToJava.set(true)
        precomputeOperations.set(true)
        cacheEnvironment.set(true)
        optimizeClassLoading.set(true)
        deduceEnvironment.set(true)
    }
}
