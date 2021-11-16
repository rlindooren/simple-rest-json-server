val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val clikt_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.0"
}

group = "nl.lindooren"
version = "0.0.1"
application {
    mainClass.set("ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation ("io.ktor:ktor-html-builder:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.github.ajalt.clikt:clikt:$clikt_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
