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
            versions("1.21.1", "1.21.11")
        }
        branch("fabric") {
            versions("1.21.1", "1.21.11")
        }
        branch("neoforge") {
            versions("1.21.1", "1.21.11")
        }
    }
}

rootProject.name = "imguimc"