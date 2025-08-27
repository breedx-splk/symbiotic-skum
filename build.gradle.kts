plugins {
    java
}

version = "0.1.0"

val otelCoreVersion = "1.53.0"
val otelCoreAlphaVersion = "1.53.0-alpha"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(platform("io.opentelemetry:opentelemetry-bom-alpha:$otelCoreAlphaVersion"))
    compileOnly("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure")
    compileOnly("com.google.auto.service:auto-service:1.1.1")

    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
    implementation("io.opentelemetry:opentelemetry-sdk-trace:${otelCoreVersion}")
}