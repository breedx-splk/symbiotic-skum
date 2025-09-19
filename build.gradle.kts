plugins {
    java
}

version = "0.1.0"

val otelCoreVersion = "1.54.1"
val otelCoreAlphaVersion = "${otelCoreVersion}-alpha"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(platform("io.opentelemetry:opentelemetry-bom-alpha:$otelCoreAlphaVersion"))
    compileOnly("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure")
    compileOnly("com.google.auto.service:auto-service:1.1.1")
    compileOnly("io.opentelemetry:opentelemetry-exporter-otlp-common:${otelCoreVersion}")

    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
    implementation("io.opentelemetry:opentelemetry-sdk-trace:${otelCoreVersion}")

    testImplementation("io.opentelemetry:opentelemetry-sdk")
    testImplementation("io.opentelemetry:opentelemetry-exporter-otlp:${otelCoreVersion}")
    testImplementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure:${otelCoreVersion}")
}