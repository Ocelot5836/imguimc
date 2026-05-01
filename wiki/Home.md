# ImGuiMC

ImGuiMC wraps [Dear ImGui](https://github.com/ocornut/imgui) for use in Minecraft mods. Use it as an optional dependency, or shadow it if you need ImGui bundled directly into your jar.

The latest release and changelog live in the [README](https://github.com/FoundryMC/imguimc/blob/main/README.md). Artifacts are on [RyanHCode's Maven](https://maven.ryanhcode.dev/#/releases/foundry/imguimc/).

---

## Setup

Pick whichever platform matches your project. The repository block is the same for all three — only the dependency line differs.

### NeoForge

<details>
<summary>Click to expand</summary>

```groovy
repositories {
    exclusiveContent {
        forRepository {
            maven {
                url = "https://maven.ryanhcode.dev/releases"
                name = "RyanHCode Maven"
            }
        }
        filter { includeGroup("foundry.imguimc") }
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
    exclusiveContent {
        forRepository {
            maven {
                url = "https://maven.ryanhcode.dev/releases"
                name = "RyanHCode Maven"
            }
        }
        filter { includeGroup("foundry.imguimc") }
    }
}

dependencies {
    modCompileOnly("foundry.imguimc:imguimc-fabric-${project.minecraft_version}:${project.imguimc_version}")
}
```

</details>

### Common (multiplatform)

<details>
<summary>Click to expand</summary>

```groovy
repositories {
    exclusiveContent {
        forRepository {
            maven {
                url = "https://maven.ryanhcode.dev/releases"
                name = "RyanHCode Maven"
            }
        }
        filter { includeGroup("foundry.imguimc") }
    }
}

dependencies {
    compileOnly("foundry.imguimc:imguimc-common-${project.minecraft_version}:${project.imguimc_version}")
}
```

</details>

---

## What's included

- **[Text Editor](Text-Editor)** — a code editor widget with syntax highlighting, autocomplete, undo/redo, and theming. Comes with built-in presets for GLSL, HLSL, and MSL, plus a clean interface for adding your own language.

- **[Gizmos & Display Panels](Gizmos)** — a 3D transform gizmo (translate/rotate/scale/bounds) with an orbit camera, view cube, snapping, grid, mini-bar, and a full theming system.