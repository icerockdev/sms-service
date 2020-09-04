/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("kotlin-kapt")
    id("maven-publish")
    id("java-library")
}

apply(plugin = "java")
apply(plugin = "kotlin")

group = "com.icerockdev.service"
version = "0.0.2"

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets.main.get().allSource)
}

dependencies {
    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${properties["kotlin_version"]}")
    // logging
    implementation("ch.qos.logback:logback-classic:${properties["logback_version"]}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${properties["coroutines_version"]}")

    // Twilio
    api("com.twilio.sdk:twilio:${properties["twilio_version"]}")

    // Ktor
    implementation("io.ktor:ktor-client-core:${properties["ktor_version"]}")
    implementation("io.ktor:ktor-client-apache:${properties["ktor_version"]}")

    // Xml support
    implementation(group = "com.fasterxml.jackson.dataformat", name = "jackson-dataformat-xml", version = "${properties["jackson_version"]}")
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = "${properties["jackson_version"]}")

    // tests
    testImplementation("io.ktor:ktor-server-tests:${properties["ktor_version"]}")
    testImplementation("io.ktor:ktor-client-mock:${properties["ktor_version"]}")
    testImplementation("io.ktor:ktor-client-mock-jvm:${properties["ktor_version"]}")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

repositories {
    mavenCentral()
}

publishing {
    repositories.maven("https://api.bintray.com/maven/icerockdev/backend/sms-service/;publish=1") {
        name = "bintray"

        credentials {
            username = System.getProperty("BINTRAY_USER")
            password = System.getProperty("BINTRAY_KEY")
        }
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}
