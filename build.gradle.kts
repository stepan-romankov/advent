import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.10"
}

group = "com.zero"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(kotlin("stdlib-jdk8"))
    api(group = "org.rocksdb", name = "rocksdbjni", version = "5.17.2")
    api(group = "com.fasterxml.jackson.dataformat", name = "jackson-dataformat-yaml", version = "2.9.2")
    api(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.9.7")
    api(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = "2.9.7")
    api(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = "2.9.7")

    testApi(group = "org.junit.jupiter", name = "junit-jupiter-api", version = "5.3.2")


}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}