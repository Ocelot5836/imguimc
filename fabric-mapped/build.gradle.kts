import me.modmuss50.mpp.ReleaseType
import org.gradle.internal.extensions.stdlib.capitalized

plugins {
    id("net.fabricmc.fabric-loom-remap")
    `maven-publish`
    id("me.modmuss50.mod-publish-plugin")
    id("multiloader-loader")
}

loom {
    fabricModJsonPath = project(":fabric-mapped").file("src/main/resources/fabric.mod.json")
    accessWidenerPath = project(":fabric-mapped").file("src/main/resources/${property("mod.id")}.classtweaker")

    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1") // Adds names to lambdas - useful for mixins
    }

    runConfigs.remove(runConfigs["server"])
    runConfigs.all {
        configName = "Fabric ${environment.capitalized()}"
        ideConfigGenerated(true)
        vmArgs("-Dmixin.debug.export=true") // Exports transformed classes for debugging
        runDir = "../../run" // Shares the run directory between versions
    }
}

repositories {
    maven("https://maven.parchmentmc.org") {
        name = "ParchmentMC"
    }
}

dependencies {
    /**
     * Fetches only the required Fabric API modules to not waste time downloading all of them for each version.
     * @see <a href="https://github.com/FabricMC/fabric">List of Fabric API modules</a>
     */
    fun fapi(vararg modules: String) {
        for (it in modules) {
            modImplementation(fabricApi.module(it, property("deps.fabric_api") as String))
            include(fabricApi.module(it, property("deps.fabric_api") as String))
        }
    }

    minecraft("com.mojang:minecraft:${sc.current.version}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${property("parchment.minecraft")}:${property("parchment.version")}@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")

    fapi("fabric-api-base", "fabric-resource-loader-v0")

    api("io.github.spair:imgui-java-binding:${project.property("deps.imgui")}")
    include("io.github.spair:imgui-java-binding:${project.property("deps.imgui")}")

    runtimeOnly("io.github.spair:imgui-java-natives-linux:${project.property("deps.imgui")}")
    runtimeOnly("io.github.spair:imgui-java-natives-macos:${project.property("deps.imgui")}")
    runtimeOnly("io.github.spair:imgui-java-natives-windows:${project.property("deps.imgui")}")
    include("io.github.spair:imgui-java-natives-linux:${project.property("deps.imgui")}")
    include("io.github.spair:imgui-java-natives-macos:${project.property("deps.imgui")}")
    include("io.github.spair:imgui-java-natives-windows:${project.property("deps.imgui")}")
}

tasks {
    // Builds the version into a shared folder in `build/libs/${mod version}/`
    register<Copy>("buildAndCollect") {
        group = "build"
        from(remapJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

publishMods {
    file = tasks.remapJar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.remapSourcesJar.map { it.archiveFile.get() })
    displayName = "${property("mod.name")} ${property("mod.version")} for ${property("mod.mc_title")}"
    version = property("mod.version") as String
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = ReleaseType.STABLE
    modLoaders.add("fabric")

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
