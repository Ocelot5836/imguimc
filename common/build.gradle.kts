plugins {
    id("multiloader-common")
    id("net.neoforged.moddev")
}

neoForge {
    neoFormVersion = property("deps.neoform") as String

    if (sc.current.parsed < "26.1") {
        parchment {
            minecraftVersion = property("parchment.minecraft") as String
            mappingsVersion = property("parchment.version") as String
        }
    }
}

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")
    // fabric and neoforge both bundle mixinextras, so it is safe to use it in common
    compileOnly("io.github.llamalad7:mixinextras-common:0.3.5")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.5")

    api("io.github.spair:imgui-java-binding:${project.property("deps.imgui")}")
}

configurations {
    register("commonJava") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    register("commonResources") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}

artifacts {
    sourceSets.main.get().java.sourceDirectories.forEach {
        add("commonJava", it)
    }
    sourceSets.main.get().resources.sourceDirectories.forEach {
        add("commonResources", it)
    }
}
