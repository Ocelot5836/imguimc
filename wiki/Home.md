# Overview

ImGuiMC is intended to be used as an optional mod. However it can be shadowed if ImGui is necessary. 


# Getting Started

The latest version can be found in the ImGuiMC [README](https://github.com/FoundryMC/imguimc/blob/main/README.md) or
directly from [RyanHCode's Maven](https://maven.ryanhcode.dev/#/releases/foundry/imguimc/).

### Neoforge

<details>
  <summary>Click to expand</summary>

```groovy
repositories {
    exclusiveContent { // ImGuiMC
        forRepository {
            maven {
                url = "https://maven.ryanhcode.dev/releases"
                name = "RyanHCode Maven"
            }
        }
        filter {
            includeGroup("foundry.imguimc")
        }
    }
}

dependencies {
    compileOnly("foundry.imguimc:imguimc-neoforge-${project.minecraft_version}:${project.imguimc_version}")
}
```

</details>

### Fabric

<details>
  <summary>Click to expand</summary>

```groovy
repositories {
    exclusiveContent { // ImGuiMC
        forRepository {
            maven {
                url = "https://maven.ryanhcode.dev/releases"
                name = "RyanHCode Maven"
            }
        }
        filter {
            includeGroup("foundry.imguimc")
        }
    }
}

dependencies {
    modCompileOnly("foundry.imguimc:imguimc-fabric-${project.minecraft_version}:${project.imguimc_version}")
}
```

</details>

### Common

<details>
  <summary>Click to expand</summary>

```groovy
repositories {
    exclusiveContent { // ImGuiMC
        forRepository {
            maven {
                url = "https://maven.ryanhcode.dev/releases"
                name = "RyanHCode Maven"
            }
        }
        filter {
            includeGroup("foundry.imguimc")
        }
    }
}

dependencies {
    compileOnly("foundry.imguimc:imguimc-common-${project.minecraft_version}:${project.imguimc_version}")
}
```

</details>
