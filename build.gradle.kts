import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.graphstream:gs-core:2.0")
    implementation("org.graphstream:gs-ui-swing:2.0")
    implementation("com.formdev:flatlaf:2.0-rc1")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.6.0")
    implementation(kotlin("script-runtime"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}