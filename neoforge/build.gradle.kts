import org.gradle.internal.extensions.stdlib.capitalized

plugins {
    id("multiloader-loader")
    id("net.neoforged.moddev")
}

neoForge {
    version = property("deps.neoforge") as String

    interfaceInjectionData.from(project(":common").file("interfaces.json"))

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
        add(
            "clientAdditionalRuntimeClasspath",
            "io.github.spair:imgui-java-natives-linux:${project.property("deps.imgui")}"
        )
        add(
            "clientAdditionalRuntimeClasspath",
            "io.github.spair:imgui-java-natives-macos:${project.property("deps.imgui")}"
        )
        add(
            "clientAdditionalRuntimeClasspath",
            "io.github.spair:imgui-java-natives-windows:${project.property("deps.imgui")}"
        )
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
