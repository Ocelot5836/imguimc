plugins {
    id("dev.kikugie.stonecutter")
    id("net.neoforged.moddev") version "2.0.140" apply false
    id("net.fabricmc.fabric-loom-remap") version "1.15-SNAPSHOT" apply false
    id("me.modmuss50.mod-publish-plugin") version "1.0.+" apply false

    id("com.google.devtools.ksp") version "2.3.7" apply false
    id("dev.kikugie.fletching-table") version "0.1.0-alpha.22" apply false
    id("dev.kikugie.fletching-table.neoforge") version "0.1.0-alpha.22" apply false
    id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.22" apply false
}

stonecutter active "1.21.11"

/*
// Make newer versions be published last
stonecutter tasks {
    order("publishModrinth")
    order("publishCurseforge")
}
 */

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    var platform = node.project.parent!!.name

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
