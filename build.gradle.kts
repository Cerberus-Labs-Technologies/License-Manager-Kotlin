plugins {
    kotlin("jvm") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "tech.cerberusLabs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))

    // gson
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("com.github.TheFruxz:Ascend:21.0.0")
    // kotlinx.serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

tasks.test {
    useJUnitPlatform()
}

tasks {

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }

}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
