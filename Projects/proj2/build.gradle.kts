import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

defaultTasks("clean", "run")

plugins {
	kotlin("jvm") version "1.6.10"
	application
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

application {
	mainClass.set("NetworkKt")
}

tasks.test {
	useJUnit()
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "1.8"
}

tasks.jar {
	manifest {
		attributes["Main-Class"] = "NetworkKt"
	}
	configurations["compileClasspath"].forEach { file: File ->
		from(zipTree(file.absoluteFile))
	}
	duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
