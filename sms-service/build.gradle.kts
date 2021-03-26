import java.util.Base64
import kotlin.text.String
/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("kotlin-kapt")
    id("maven-publish")
    id("java-library")
    id("signing")
}

apply(plugin = "java")
apply(plugin = "kotlin")

group = "com.icerockdev.service"
version = "0.1.1"

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

dependencies {
    // logging
    implementation("ch.qos.logback:logback-classic:${properties["logback_version"]}")

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
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withJavadocJar()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

repositories {
    mavenCentral()
}

publishing {
    repositories.maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
        name = "OSSRH"

        credentials {
            username = System.getenv("OSSRH_USER")
            password = System.getenv("OSSRH_KEY")
        }
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
            pom {
                name.set("Sms service")
                description.set("Sms sending service for kotlin")
                url.set("https://github.com/icerockdev/sms-service")
                licenses {
                    license {
                        url.set("https://github.com/icerockdev/sms-service/blob/master/LICENSE.md")
                    }
                }

                developers {
                    developer {
                        id.set("YokiToki")
                        name.set("Stanislav")
                        email.set("skarakovski@icerockdev.com")
                    }

                    developer {
                        id.set("AlexeiiShvedov")
                        name.set("Alex Shvedov")
                        email.set("ashvedov@icerockdev.com")
                    }

                    developer {
                        id.set("oyakovlev")
                        name.set("Oleg Yakovlev")
                        email.set("oyakovlev@icerockdev.com")
                    }
                }

                scm {
                    connection.set("scm:git:ssh://github.com/icerockdev/sms-service.git")
                    developerConnection.set("scm:git:ssh://github.com/icerockdev/sms-service.git")
                    url.set("https://github.com/icerockdev/sms-service")
                }
            }
        }

        signing {
            val signingKeyId: String? = System.getenv("SIGNING_KEY_ID")
            val signingPassword: String? = System.getenv("SIGNING_PASSWORD")
            val signingKey: String? = System.getenv("SIGNING_KEY")?.let { base64Key ->
                String(Base64.getDecoder().decode(base64Key))
            }
            useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
            sign(publishing.publications["mavenJava"])
        }
    }
}
