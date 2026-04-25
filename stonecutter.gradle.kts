plugins {
    id("dev.kikugie.stonecutter")
    id("net.neoforged.moddev") version "2.0.140" apply false
    id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT" apply false
    id("me.modmuss50.mod-publish-plugin") version "1.0.+" apply false
}

stonecutter active "1.21.1"

// Make newer versions published last
if (project.parent != null && !project.parent!!.name.equals("common", ignoreCase = true)) {
    stonecutter tasks {
        order("publishModrinth")
        order("publishCurseforge")
        order("publishGithub")
    }
}

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    var platform = node.project.parent!!.name.split("-")[0]

    swaps["mod_version"] = "\"${property("mod.version")}\";"
    swaps["minecraft"] = "\"${node.metadata.version}\";"
    swaps["platform"] = "\"${platform}\";"
    constants["release"] = property("mod.id") != "template"
    if (platform.equals("fabric", ignoreCase = true)) {
        dependencies["fapi"] = node.project.property("deps.fabric_api") as String
    }
    if (platform.equals("neoforge", ignoreCase = true)) {
        dependencies["neoforge"] = node.project.property("deps.neoforge") as String
    }

    replacements {
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
        }
    }
}
