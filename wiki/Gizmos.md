# Gizmos & Display Panels

A 3D transform gizmo you can drop into any ImGui window. Supports translate, rotate, scale, universal, and bounds operations. Ships with an orbit camera, a view-orientation cube, a floating mini-bar for switching modes, a configurable grid, and a theming system.

![Gizmo Dark Theme](https://github.com/Manyaarvadiya/imguimc/blob/22d3344e81209d085c9d1d726efb465daf862b64/wiki/screenshots/guizmo/dark.png?raw=true)

---

## Basic setup

```java
// create once, hold onto it during your render loop
ImguiGizmoScreen gizmo = new ImguiGizmoScreen("myGizmo")
        .setConfig(ImguiGizmoConfig.universal().build())
        .setTheme(ImguiGizmoTheme.dark().build());

// every frame inside your ImGui draw callback
gizmo.render();

// read back the updated model matrix after rendering
Matrix4f model = gizmo.getMatrices().getModelMatrix();
```

`ImguiGizmoScreen` fills all available space by default. Pin it to a fixed size if you need to:

```java
gizmo.setSize(400f, 300f);
```

---

## Config

`ImguiGizmoConfig` is immutable. Start from one of the presets, chain whatever you want to change, and call `.build()`.

```java
// presets
ImguiGizmoConfig.universal()   // UNIVERSAL op, WORLD mode, grid on
ImguiGizmoConfig.translate()   // TRANSLATE op, LOCAL mode, grid on
ImguiGizmoConfig.rotate()      // ROTATE op,    LOCAL mode, grid off
ImguiGizmoConfig.scale()       // SCALEU op,    LOCAL mode, grid on
ImguiGizmoConfig.bounds()      // BOUNDS op,    LOCAL mode, default AABB
ImguiGizmoConfig.light()       // ROTATE op,    WORLD mode, light mode flag set

// tweak before building
ImguiGizmoConfig config = ImguiGizmoConfig.translate()
        .withSnapTranslation(true, 0.5f)   // snap every 0.5 units
        .withSnapRotation(true, 15f)        // snap every 15°
        .withSnapScale(true, 0.1f)
        .withGridSize(1f)
        .withShowGrid(true)
        .withOrthographic(false)
        .withLockTransform(false)           // true = view only, no manipulation
        .withLockView(false)                // true = freeze the camera
        .withViewCubeArmLength(8f)
        .build();

gizmo.setConfig(config);
```

You can also flip the active operation at runtime without rebuilding the whole config:

```java
gizmo.setRuntimeOperation(Operation.ROTATE);
gizmo.setRuntimeGridSize(0.5f);
```

Runtime values win over config values. Pass `-1` to clear them.

---

## Camera

The built-in camera is a standard orbit camera — right-drag to orbit, scroll to zoom.

```java
ImguiGizmoCamera camera = new ImguiGizmoCamera()
        .setYaw(45f)
        .setPitch(30f)
        .setDistance(5f)
        .setFovDeg(45f)
        .setNearFar(0.1f, 500f)
        .setDistanceLimits(0.5f, 100f)
        .setOrbitSensitivity(0.5f)   // degrees per pixel
        .setZoomSensitivity(0.5f)    // world units per scroll notch
        .setOrthographic(false);

gizmo.setCamera(camera);

// switch to ortho at runtime
camera.setOrthographic(true).setOrthoScale(5f);

// snap the camera to a view matrix (e.g. after the user clicks the view cube)
camera.syncFromViewMatrix(someViewMatrix);

// reset to defaults
camera.reset();
```

Lock the camera so it ignores mouse input:

```java
camera.setLocked(true);
// or lock just for this frame via config:
gizmo.setConfig(gizmo.getConfig().toBuilder().withLockView(true).build());
```

---

## Mini-bar

The mini-bar is a small floating overlay that lets the user switch gizmo operations with one click. Attach it with `setMiniBar()`.

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

Default button labels:

| Label | Operation | Tooltip |
|-------|-----------|---------|
| `P` | `TRANSLATE` | Translate  [P] |
| `R` | `ROTATE`    | Rotate     [R] |
| `S` | `SCALE`     | Scale      [S] |
| `U` | `UNIVERSAL` | Universal  [U] |
| `B` | `BOUNDS`    | Bounds     [B] |

Show only a subset:

```java
miniBar.enableOnly(Operation.TRANSLATE, Operation.ROTATE);
```

Disable a button without hiding it (deselects the operation if it was active):

```java
miniBar.setEnabled(Operation.SCALE, false);
```

Read the current selection:

```java
int op = miniBar.getSelectedOperation(); // -1 if nothing selected
```

The mini-bar selection takes priority over `runtimeOperation` and `config.operation` when the gizmo resolves which operation to draw.

You can also render the mini-bar as a standalone widget, detached from any screen — useful if you're building a custom toolbar:

```java
miniBar.renderStandalone(wx, wy, ww, wh);
// with a custom theme:
miniBar.renderStandalone(wx, wy, ww, wh, myTheme);
```

---

## Reading & writing matrices

```java
ImguiGizmoMatrices matrices = gizmo.getMatrices();

// push a model matrix in
gizmo.setModelMatrix(someMatrix4f);

// read back after render (JOML Matrix4f)
Matrix4f model = matrices.getModelMatrix();
Matrix4f view  = matrices.getViewMatrix();
Matrix4f proj  = matrices.getProjMatrix();

// raw float[16] access — column-major, matches OpenGL / ImGuizmo convention
float[] rawModel = matrices.getModelRaw();
```

Check whether the user actually dragged the gizmo this frame:

```java
if (gizmo.wasImguiGizmoUsed()) {
    // model matrix changed — sync back to your scene object
}
```

### Model scale

If your mesh has a uniform visual scale other than 1, lock it so the gizmo doesn't mess with it during normalization:

```java
gizmo.setModelScale(2.5f); // keeps model columns normalized to length 2.5
```

Pass `1f` (the default) to leave it alone.

---

## Texture overlay

You can render a GPU texture inside the viewport and draw the gizmo on top — handy for showing an offline-rendered or framebuffer image:

```java
gizmo.setTextureId(myOpenGLTextureId);
gizmo.setFlipTextureY(true); // flip the V axis — needed for most OpenGL framebuffers
```

---

## Registry

`ImguiGizmoRegistry` is an optional singleton for tracking named gizmo instances. Mostly useful when multiple panels share a viewport, or when you need to look up a gizmo from code that doesn't hold a direct reference.

```java
ImguiGizmoRegistry registry = ImguiGizmoRegistry.getInstance();

registry.register("viewport", myGizmoScreen);

// create one with defaults on first access
ImguiGizmoScreen gizmo = registry.getOrCreate("viewport");

// look it up later
ImguiGizmoScreen gizmo = registry.get("viewport");

registry.remove("viewport");
registry.clear();

for (ImguiGizmoScreen s : registry.all()) {
    s.render();
}
```

---

## Themes

`ImguiGizmoTheme` covers every visual property of the screen — background, border, grid, view cube tint, overlay text, and all mini-bar colors. Colors are packed ABGR ints (`0xAABBGGRR`), same as everywhere else in ImGui.

```java
// use a preset
ImguiGizmoTheme theme = ImguiGizmoTheme.dark().build();
ImguiGizmoTheme theme = ImguiGizmoTheme.warm().build();

// tweak a preset
ImguiGizmoTheme theme = ImguiGizmoTheme.dark()
        .withBackgroundArgb(0xE6120D0A)
        .withBorderArgb(0xCC7A5020)
        .withBorderThickness(1.5f)
        .withBorderRounding(5f)
        .withGridColor(0xFF443830)
        .withGridSize(1f)
        .withGridVisible(true)
        .withViewCubeBackground(0x00000000)  // transparent
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

`ImguiGizmoTheme` is immutable — every `with*` call returns a new instance, so it's safe to hold multiple themes and swap between them at runtime.

> **Note:** `translationLineThickness`, `rotationLineThickness`, `scaleLineThickness`, and the related line style fields are stored but not yet wired up — they're placeholders for a future update.

### Built-in themes

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

## Full example

```java
// --- setup (once) ---
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

// --- every frame ---
ImGui.begin("3D Viewport");
gizmo.render();
ImGui.end();

// --- sync back ---
if (gizmo.wasImguiGizmoUsed()) {
    myEntity.setModelMatrix(gizmo.getMatrices().getModelMatrix());
}
```
