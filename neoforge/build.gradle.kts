import org.gradle.internal.extensions.stdlib.capitalized
import java.util.Locale
import java.util.Locale.getDefault

plugins {
    id("multiloader-loader")
    id("net.neoforged.moddev")
//     `maven-publish`
//     id("me.modmuss50.mod-publish-plugin")
}

neoForge {
    version = property("deps.neoforge") as String

    if (sc.current.parsed < "26.1") {
        parchment {
            minecraftVersion = property("parchment.minecraft") as String
            mappingsVersion = property("parchment.version") as String
        }
    }

    runs {
        configureEach {
            systemProperty("neoforge.enabledGameTestNamespaces", project.property("mod.id") as String)
            ideName = "NeoForge ${name.capitalized()} (${project.path})" // Unify the run config names with fabric
        }

        register("client") {
            gameDirectory = file("../../run/")
            client()
        }

        register("server") {
            gameDirectory = file("../../run/")
            server()
        }
    }

    mods {
        register("${project.property("mod.id")}") {
            sourceSet(sourceSets.main.get())
        }
    }
}

dependencies {
    api("io.github.spair:imgui-java-binding:${project.property("deps.imgui")}")
    jarJar("io.github.spair:imgui-java-binding:${project.property("deps.imgui")}")

    runtimeOnly("io.github.spair:imgui-java-natives-linux:${project.property("deps.imgui")}")
    runtimeOnly("io.github.spair:imgui-java-natives-macos:${project.property("deps.imgui")}")
    runtimeOnly("io.github.spair:imgui-java-natives-windows:${project.property("deps.imgui")}")
    jarJar("io.github.spair:imgui-java-natives-linux:${project.property("deps.imgui")}")
    jarJar("io.github.spair:imgui-java-natives-macos:${project.property("deps.imgui")}")
    jarJar("io.github.spair:imgui-java-natives-windows:${project.property("deps.imgui")}")

    if (sc.current.parsed <= "1.21.8") {
        add("clientAdditionalRuntimeClasspath", "io.github.spair:imgui-java-binding:${project.property("deps.imgui")}")
        add("clientAdditionalRuntimeClasspath", "io.github.spair:imgui-java-natives-linux:${project.property("deps.imgui")}")
        add("clientAdditionalRuntimeClasspath", "io.github.spair:imgui-java-natives-macos:${project.property("deps.imgui")}")
        add("clientAdditionalRuntimeClasspath", "io.github.spair:imgui-java-natives-windows:${project.property("deps.imgui")}")
    }
}

tasks {
    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    // Builds the version into a shared folder in `build/libs/${mod version}/`
    register<Copy>("buildAndCollect") {
        group = "build"
        from(jar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

/*
// Publishes builds to Modrinth and Curseforge with changelog from the CHANGELOG.md file
publishMods {
    file = tasks.jar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.sourcesJar.map { it.archiveFile.get() })
    displayName = "${property("mod.name")} ${property("mod.version")} for ${property("mod.mc_title")}"
    version = property("mod.version") as String
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE
    modLoaders.add("neoforge")

    dryRun = providers.environmentVariable("MODRINTH_TOKEN").getOrNull() == null
        || providers.environmentVariable("CURSEFORGE_TOKEN").getOrNull() == null

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.addAll(property("mod.mc_targets").toString().split(' '))
    }

    curseforge {
        projectId = property("publish.curseforge") as String
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.addAll(property("mod.mc_targets").toString().split(' '))
    }
}
 */
/*
// Publishes builds to a maven repository under `com.example:template:0.1.0+mc`
publishing {
    repositories {
        maven("https://maven.example.com/releases") {
            name = "myMaven"
            // To authenticate, create `myMavenUsername` and `myMavenPassword` properties in your Gradle home properties.
            // See https://stonecutter.kikugie.dev/wiki/tips/properties#defining-properties
            credentials(PasswordCredentials::class.java)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "${property("mod.group")}.${property("mod.id")}"
            artifactId = property("mod.id") as String
            version = project.version

            from(components["java"])
        }
    }
}
 */