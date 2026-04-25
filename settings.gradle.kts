pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        repositories {
            maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
        }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9"
    id("com.possible-triangle.helper") version "1.3"
}

stonecutter {
    create(rootProject) {
        branch("common") {
            versions("1.21.1", "1.21.11", "26.1", "26.2-snapshot-3", "26.2-snapshot-4")
        }
        branch("fabric-mapped") {
            versions("1.21.1", "1.21.11")
        }
        branch("fabric") {
            versions("26.1", "26.2-snapshot-3")
        }
        branch("neoforge") {
            versions("1.21.1", "1.21.11", "26.1")
        }
    }
}

rootProject.name = "imguimc"