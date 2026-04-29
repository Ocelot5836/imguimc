# Overview

ImGuiMC is intended to be used as an optional mod, but can be shadowed if ImGui is required in your project.

# Getting Started

The latest version can be found in the ImGuiMC [README](https://github.com/FoundryMC/imguimc/blob/main/README.md) or directly from [RyanHCode's Maven](https://maven.ryanhcode.dev/#/releases/foundry/imguimc/).

### Neoforge

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

### Common

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

# Text Editor

<details>
<summary>Click to expand</summary>

A fully-featured code editor widget with syntax highlighting, autocomplete, undo/redo, mouse selection, and keyboard shortcuts. Everything is pluggable — bring your own colorizer and autocomplete provider, or use one of the built-in presets for GLSL, HLSL, and MSL.

## Preset Editors

<table>
<tr>
<td align="center" width="33%">
<b>GLSLTextEditor</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/63b512b5e9e2c760ce61fc484e61eaef08a0f90a/wiki/screenshots/texteditor/GLSL.png?raw=true" width="220"/>
</td>
<td align="center" width="33%">
<b>HLSLTextEditor</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/63b512b5e9e2c760ce61fc484e61eaef08a0f90a/wiki/screenshots/texteditor/HLSL.png?raw=true" width="220"/>
</td>
<td align="center" width="33%">
<b>MSLTextEditor</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/63b512b5e9e2c760ce61fc484e61eaef08a0f90a/wiki/screenshots/texteditor/MSL.png?raw=true" width="220"/>
</td>
</tr>
</table>

All three share the same API:

```java
// Default dark theme
GLSLTextEditor editor = new GLSLTextEditor();

// Or pass any theme
HLSLTextEditor editor = new HLSLTextEditor(EditorTheme.monokai().build());

editor.setText(myShaderSource);

// Call every frame inside your ImGui draw callback
editor.render("##myEditor", width, height, false);

// Read back content any time
String source = editor.getText();

// Other available methods
editor.setReadOnly(true);
int lines = editor.getTotalLines();

// Access internals if needed
ImguiCoreTextEditor core = editor.getCore();
GLSLColorizer colorizer  = editor.getColorizer();
```

---

## Themes

All built-in themes are static methods on `EditorTheme` returning a `Builder`. Call `.build()` to finalize.

```java
// Use a preset as-is
EditorTheme theme = EditorTheme.dracula().build();

// Tweak a preset before building
EditorTheme theme = EditorTheme.dark()
        .withCursorColor(0xFF00FFFF)
        .withTabSize(2)
        .withLineSpacing(1.2f)
        .withCursorBlinkMs(500)  // 0 disables blinking
        .build();

// Build from scratch
EditorTheme theme = new EditorTheme.Builder()
        .withBackgroundColor(0xFF1E1E2E)
        .withCurrentLineColor(0x18FFFFFF)
        .withSelectionColor(0x806060AA)
        .withCursorColor(0xFFCDD6F4)
        .withCursorBlinkMs(1000)
        .withCursorWidth(2f)
        .withLineNumberColor(0xFF6C7086)
        .withGutterSeparatorColor(0xFF313244)
        .withGutterPaddingRight(10f)
        .withHScrollbarHeight(10f)
        .withLineSpacing(1.0f)
        .withTabSize(4)
        .build();
```

All colors are packed ABGR ints.

<table>
<tr>
<td align="center" width="50%">
<b>dark()</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/63b512b5e9e2c760ce61fc484e61eaef08a0f90a/wiki/screenshots/texteditor/dark.png?raw=true" width="340"/>
</td>
<td align="center" width="50%">
<b>monokai()</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/63b512b5e9e2c760ce61fc484e61eaef08a0f90a/wiki/screenshots/texteditor/monokai.png?raw=true" width="340"/>
</td>
</tr>
<tr>
<td align="center">
<b>dracula()</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/63b512b5e9e2c760ce61fc484e61eaef08a0f90a/wiki/screenshots/texteditor/dracula.png?raw=true" width="340"/>
</td>
<td align="center">
<b>nord()</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/63b512b5e9e2c760ce61fc484e61eaef08a0f90a/wiki/screenshots/texteditor/nord.png?raw=true" width="340"/>
</td>
</tr>
<tr>
<td align="center">
<b>oneDark()</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/63b512b5e9e2c760ce61fc484e61eaef08a0f90a/wiki/screenshots/texteditor/onedark.png?raw=true" width="340"/>
</td>
<td align="center">
<b>solarizedDark()</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/63b512b5e9e2c760ce61fc484e61eaef08a0f90a/wiki/screenshots/texteditor/solarized_dark.png?raw=true" width="340"/>
</td>
</tr>
<tr>
<td align="center" colspan="2">
<b>githubLight()</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/63b512b5e9e2c760ce61fc484e61eaef08a0f90a/wiki/screenshots/texteditor/github_light.png?raw=true" width="340"/>
</td>
</tr>
</table>

---

## Custom Editor

If none of the presets cover your language, implement `IEditorColorizer` and `IAutocompleteProvider` and pass them into `ImguiCoreTextEditor` directly. The example below is a custom colorizer built for a scripting language called Shard.

![Custom Editor Example](https://github.com/Manyaarvadiya/imguimc/blob/63b512b5e9e2c760ce61fc484e61eaef08a0f90a/wiki/screenshots/texteditor/shard.png?raw=true)

### Colorizer

Extend `AbstractBaseColorizer` — it handles the per-line dirty cache so you only need to fill in two methods.

```java
public class MyColorizer extends AbstractBaseColorizer {

    public static final int COLOR_DEFAULT = 0xFFD4D4D4;
    public static final int COLOR_KEYWORD = 0xFF569CD6;
    public static final int COLOR_COMMENT = 0xFF6A9955;
    public static final int COLOR_NUMBER  = 0xFFB5CEA8;

    private static final Set<String> KEYWORDS = Set.of("if", "else", "for", "while", "return");

    @Override
    public int getDefaultColor() { return COLOR_DEFAULT; }

    // Runs once after setText / invalidateAll.
    // Scan the whole document here for user-defined structs, functions, etc.
    @Override
    protected void analyzeDocument(List<List<EditorGlyph>> lines) { }

    // Called per visible line. Set glyph.color for each character index.
    @Override
    protected void colorizeLineImpl(List<EditorGlyph> line, int lineIdx, String text) {
        int commentAt = findLineCommentStart(text); // provided by base class
        String code = commentAt >= 0 ? text.substring(0, commentAt) : text;

        // tokenize 'code' and assign colors ...

        if (commentAt >= 0)
            for (int i = commentAt; i < line.size(); i++)
                line.get(i).color = COLOR_COMMENT;
    }
}
```

`glyphsToString(line)` converts a glyph list to a plain String. `findLineCommentStart(text)` returns the index of the first `//` not inside a string literal, or -1.

### Autocomplete Provider

```java
public class MyAutocompleteProvider implements IAutocompleteProvider {

    @Override public int     minPrefixLength() { return 2; }
    @Override public boolean appendParens()    { return true; } // adds "()" after functions

    @Override
    public boolean shouldSuppress(String prefix, List<List<EditorGlyph>> lines, EditorCoordinates cursor) {
        if (cursor.line < lines.size()) {
            List<EditorGlyph> line = lines.get(cursor.line);
            int wordStart = cursor.column - prefix.length();
            if (wordStart > 0 && line.get(wordStart - 1).ch == '.') return true;
        }
        return false;
    }

    @Override
    public List<AutocompleteItem> getCandidates(String prefix, List<List<EditorGlyph>> lines, EditorCoordinates cursor) {
        String lower = prefix.toLowerCase();
        List<AutocompleteItem> out = new ArrayList<>();

        for (String kw : KEYWORDS)
            if (kw.toLowerCase().startsWith(lower))
                out.add(new AutocompleteItem(kw, "keyword", "", 0xFF569CD6));

        // valid type labels: "keyword" | "type" | "function" | "variable" | "constant" | "qualifier"
        return out;
    }
}
```

For function signatures in the popup, use `FunctionSignature`:

```java
String sig = new FunctionSignature("clamp", "float", "float x, float minVal, float maxVal").format();
// → "float clamp(float x, float minVal, float maxVal)"
```

### Putting it together

```java
MyColorizer colorizer     = new MyColorizer();
MyAutocompleteProvider ac = new MyAutocompleteProvider(); // or null for no autocomplete
EditorTheme theme         = EditorTheme.dark().build();

ImguiCoreTextEditor editor = new ImguiCoreTextEditor(colorizer, ac, theme);
editor.setText("// your content here");

// Every frame inside your ImGui callback:
// isResizing — pass true while the user drags a resize handle to suspend input
editor.render("##myEditor", availW, availH, isResizing);
```

---

## Keyboard Shortcuts

| Shortcut | Action |
|---|---|
| `Ctrl+Z` / `Ctrl+Y` | Undo / Redo |
| `Ctrl+A` | Select all |
| `Ctrl+C` / `Ctrl+X` / `Ctrl+V` | Copy / Cut / Paste |
| `Ctrl+←` / `Ctrl+→` | Jump word left / right |
| `Home` / `End` | Start / end of line |
| `Ctrl+Home` / `Ctrl+End` | Start / end of document |
| `PgUp` / `PgDn` | Page up / down |
| `Shift + movement` | Extend selection |
| `Tab` / `Enter` | Insert tab or newline; accept autocomplete item |
| `↑` / `↓` (popup open) | Navigate autocomplete list |
| `Escape` | Dismiss autocomplete popup |

Double-click selects a word, triple-click selects the whole line. The undo stack is capped at 200 entries. Call `editor.pushUndo()` before making programmatic changes if you want them to be undoable.

</details>

---

# Gizmos & Display Panels

<details>
<summary>Click to expand</summary>

A 3D transform gizmo widget that renders inside any ImGui window. It supports translate, rotate, scale, universal, and bounds operations, comes with an orbit camera, a view-orientation cube, an optional floating mini-bar for switching modes, a configurable grid, and a full theming system.

![Gizmo Dark Theme](https://github.com/Manyaarvadiya/imguimc/blob/22d3344e81209d085c9d1d726efb465daf862b64/wiki/screenshots/guizmo/dark.png?raw=true)

## Basic Setup

```java
// Create once and store somewhere accessible during your render loop
ImguiGizmoScreen gizmo = new ImguiGizmoScreen("myGizmo")
                .setConfig(ImguiGizmoConfig.universal().build())
                .setTheme(ImguiGizmoTheme.dark().build());

// Every frame, inside your ImGui draw callback
gizmo.render();

// After rendering, read back the updated model matrix
Matrix4f model = gizmo.getMatrices().getModelMatrix();
```

`ImguiGizmoScreen` fills all available space by default. You can fix the size:

```java
gizmo.setSize(400f, 300f); // width, height in pixels
```

---

## Config

`ImguiGizmoConfig` is immutable. Use one of the static presets as a starting point, tweak via the builder, then call `.build()`.

```java
// Preset starting points
ImguiGizmoConfig.universal()   // UNIVERSAL op, WORLD mode, grid on
ImguiGizmoConfig.translate()   // TRANSLATE op, LOCAL mode, grid on
ImguiGizmoConfig.rotate()      // ROTATE op,    LOCAL mode, grid off
ImguiGizmoConfig.scale()       // SCALEU op,    LOCAL mode, grid on
ImguiGizmoConfig.bounds()      // BOUNDS op,    LOCAL mode, default AABB
ImguiGizmoConfig.light()       // ROTATE op,    WORLD mode, light mode flag set

// Customise before building
ImguiGizmoConfig config = ImguiGizmoConfig.translate()
        .withSnapTranslation(true, 0.5f)   // snap every 0.5 units
        .withSnapRotation(true, 15f)        // snap every 15°
        .withSnapScale(true, 0.1f)
        .withGridSize(1f)
        .withShowGrid(true)
        .withOrthographic(false)
        .withLockTransform(false)           // true = view-only, no manipulation
        .withLockView(false)                // true = freeze camera
        .withViewCubeArmLength(8f)
        .build();

gizmo.setConfig(config);
```

You can also change the active operation at runtime without rebuilding the full config:

```java
gizmo.setRuntimeOperation(Operation.ROTATE);
gizmo.setRuntimeGridSize(0.5f);
```

Runtime values take priority over the config values and can be cleared by passing `-1`.

---

## Camera

The built-in `ImguiGizmoCamera` is an orbit camera. Right-drag to orbit, scroll to zoom.

```java
ImguiGizmoCamera camera = new ImguiGizmoCamera()
        .setYaw(45f)
        .setPitch(30f)
        .setDistance(5f)
        .setFovDeg(45f)
        .setNearFar(0.1f, 500f)
        .setDistanceLimits(0.5f, 100f)
        .setOrbitSensitivity(0.5f)   // degrees per pixel dragged
        .setZoomSensitivity(0.5f)    // world units per scroll notch
        .setOrthographic(false);

gizmo.setCamera(camera);

// Switch to orthographic projection at runtime
camera.setOrthographic(true).setOrthoScale(5f);

// Snap the camera to match a view matrix (e.g. after the user clicks the view cube)
camera.syncFromViewMatrix(someViewMatrix);

// Restore defaults
camera.reset();
```

Lock the camera so it does not respond to mouse input:

```java
camera.setLocked(true);
// or lock just for this frame via config:
gizmo.setConfig(gizmo.getConfig().toBuilder().withLockView(true).build());
```

---

## Mini-Bar

The mini-bar is a small floating overlay that lets the user switch gizmo operations with a single click. Attach one to a `ImguiGizmoScreen` with `setMiniBar()`.

```java
ImguiGizmoMiniBar miniBar = new ImguiGizmoMiniBar()
        .setAnchor(ImguiGizmoMiniBar.Anchor.BOTTOM_LEFT)  // or TOP_LEFT
        .setMargin(8f, 8f)
        .setButtonSize(26f, 22f)
        .setSpacing(2f)
        .showTranslate(true)
        .showRotate(true)
        .showScale(true)
        .showUniversal(false)
        .showBounds(false)
        .setOperation(Operation.TRANSLATE);  // pre-select an operation

gizmo.setMiniBar(miniBar);
```

Button labels and their default keyboard hint tooltips:

| Label | Operation | Tooltip |
|-------|-----------|---------|
| `P` | `TRANSLATE` | Translate  [P] |
| `R` | `ROTATE`    | Rotate     [R] |
| `S` | `SCALE`     | Scale      [S] |
| `U` | `UNIVERSAL` | Universal  [U] |
| `B` | `BOUNDS`    | Bounds     [B] |

Show only a specific subset of operations:

```java
miniBar.enableOnly(Operation.TRANSLATE, Operation.ROTATE);
```

Disable a single button without hiding it from the bar — the operation is also deselected if it was active:

```java
miniBar.setEnabled(Operation.SCALE, false);
```

Read back the currently selected operation:

```java
int op = miniBar.getSelectedOperation(); // -1 if nothing is selected
```

The mini-bar's selection takes priority over `runtimeOperation` and `config.operation` when resolving which gizmo to draw.

You can also render the mini-bar as a standalone widget (not attached to any screen), useful for custom toolbar layouts:

```java
miniBar.renderStandalone(wx, wy, ww, wh);
// or supply a custom theme:
miniBar.renderStandalone(wx, wy, ww, wh, myTheme);
```

---

## Reading & Writing Matrices

```java
ImguiGizmoMatrices matrices = gizmo.getMatrices();

// Push a model matrix into the gizmo
gizmo.setModelMatrix(someMatrix4f);

// Read back after render (JOML Matrix4f)
Matrix4f model = matrices.getModelMatrix();
Matrix4f view  = matrices.getViewMatrix();
Matrix4f proj  = matrices.getProjMatrix();

// Raw float[16] access (column-major, matches OpenGL / ImGuizmo convention)
float[] rawModel = matrices.getModelRaw();
```

Check whether the user actively dragged the gizmo this frame:

```java
if (gizmo.wasImguiGizmoUsed()) {
    // model matrix changed — sync to your scene object
}
```

### Model Scale

If your rendered mesh has a uniform visual scale that is not 1, lock that scale so the gizmo never distorts it through its own normalization:

```java
gizmo.setModelScale(2.5f); // keeps the model columns normalized to length 2.5
```

Pass `1f` (the default) to leave scaling untouched.

---

## Texture Overlay

Render a GPU texture inside the viewport and draw the gizmo on top of it — useful for displaying an offline-rendered or framebuffer image in the panel:

```java
gizmo.setTextureId(myOpenGLTextureId);
gizmo.setFlipTextureY(true); // true = flip V axis (correct for most OpenGL framebuffers)
```

---

## Registry

`ImguiGizmoRegistry` is an optional singleton for managing named gizmo instances. It is most useful when multiple panels need to share the same viewport or when you want to look up a gizmo from code that does not hold a direct reference.

```java
ImguiGizmoRegistry registry = ImguiGizmoRegistry.getInstance();

// Register explicitly
registry.register("viewport", myGizmoScreen);

// Or let the registry create one with defaults on first access
ImguiGizmoScreen gizmo = registry.getOrCreate("viewport");

// Look up later
ImguiGizmoScreen gizmo = registry.get("viewport");

// Housekeeping
registry.remove("viewport");
registry.clear();

// Iterate all registered screens
for (ImguiGizmoScreen s : registry.all()) {
    s.render();
}
```

---

## Themes

`ImguiGizmoTheme` controls every visual property of the screen — background, border, grid color, view-cube tint, overlay text, and all mini-bar colors. Colors are **packed ABGR ints** (`0xAABBGGRR`), consistent with ImGui's native format.

```java
// Use a preset
ImguiGizmoTheme theme = ImguiGizmoTheme.dark().build();
ImguiGizmoTheme theme = ImguiGizmoTheme.warm().build();

// Tweak a preset
ImguiGizmoTheme theme = ImguiGizmoTheme.dark()
        .withBackgroundArgb(0xE6120D0A)
        .withBorderArgb(0xCC7A5020)
        .withBorderThickness(1.5f)
        .withBorderRounding(5f)
        .withGridColor(0xFF443830)
        .withGridSize(1f)
        .withGridVisible(true)
        .withViewCubeBackground(0x00000000)  // 0x00000000 = transparent
        .withMiniBarBgArgb(0xD1100C09)
        .withMiniBarBorderArgb(0xCC503820)
        .withMiniBarRounding(4f)
        .withMiniBarButtonActiveArgb(0xF2622810)
        .withMiniBarButtonActiveHoverArgb(0xF27A3818)
        .withMiniBarButtonInactiveArgb(0xE61A1008)
        .withMiniBarButtonInactiveHoverArgb(0xE6281A0E)
        .withMiniBarTextActiveArgb(0xFFF5E8D8)
        .withMiniBarTextInactiveArgb(0xFF806050)
        .build();

gizmo.setTheme(theme);
```

`ImguiGizmoTheme` is immutable. Every `with*` method returns a new instance, so you can safely keep multiple theme objects and swap between them at runtime.

> **Note:** `translationLineThickness`, `rotationLineThickness`, `scaleLineThickness`, and related line style fields do not work for now — they are stored for future use.

### Built-in Themes

<table>
<tr>
<td align="center" width="33%">
<b>blueprint</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/22d3344e81209d085c9d1d726efb465daf862b64/wiki/screenshots/guizmo/blueprint.png?raw=true" width="220"/>
</td>
<td align="center" width="33%">
<b>dark</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/22d3344e81209d085c9d1d726efb465daf862b64/wiki/screenshots/guizmo/dark.png?raw=true" width="220"/>
</td>
<td align="center" width="33%">
<b>dracula</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/22d3344e81209d085c9d1d726efb465daf862b64/wiki/screenshots/guizmo/dracula.png?raw=true" width="220"/>
</td>
</tr>
<tr>
<td align="center" width="33%">
<b>light</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/22d3344e81209d085c9d1d726efb465daf862b64/wiki/screenshots/guizmo/light.png?raw=true" width="220"/>
</td>
<td align="center" width="33%">
<b>midnight</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/22d3344e81209d085c9d1d726efb465daf862b64/wiki/screenshots/guizmo/midnight.png?raw=true" width="220"/>
</td>
<td align="center" width="33%">
<b>nord</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/22d3344e81209d085c9d1d726efb465daf862b64/wiki/screenshots/guizmo/nord.png?raw=true" width="220"/>
</td>
</tr>
</table>

---

## Full Example

```java
// --- Setup (once) ---
ImguiGizmoScreen gizmo = new ImguiGizmoScreen("mainViewport")
                .setConfig(ImguiGizmoConfig.universal()
                        .withGridSize(1f)
                        .withShowGrid(true)
                        .withSnapTranslation(true, 0.5f)
                        .build())
                .setTheme(ImguiGizmoTheme.dark().build())
                .setCamera(new ImguiGizmoCamera()
                        .setYaw(45f).setPitch(30f).setDistance(5f))
                .setMiniBar(new ImguiGizmoMiniBar()
                        .showTranslate(true)
                        .showRotate(true)
                        .showScale(true));

gizmo.setModelMatrix(myEntity.getModelMatrix());

// --- Every frame inside your ImGui callback ---
        ImGui.begin("3D Viewport");
gizmo.render();
ImGui.end();

// --- Sync back ---
if (gizmo.wasImguiGizmoUsed()) {
        myEntity.setModelMatrix(gizmo.getMatrices().getModelMatrix());
        }
```

</details>
