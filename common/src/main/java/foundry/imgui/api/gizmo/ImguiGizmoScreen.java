package foundry.imgui.api.gizmo;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.extension.imguizmo.flag.Operation;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Matrix4f;

public final class ImguiGizmoScreen {

    private final String id;

    private ImguiGizmoConfig  config;
    private ImguiGizmoTheme   theme;
    private ImguiGizmoCamera  camera;
    private ImguiGizmoMiniBar miniBar;

    private float width  = 0f;
    private float height = 0f;

    private int     textureId    = -1;
    private boolean flipTextureY = true;

    private final ImguiGizmoMatrices matrices  = new ImguiGizmoMatrices();
    private final Matrix4f      tmpView   = new Matrix4f();
    private final Matrix4f      afterView = new Matrix4f();

    private boolean wasUsedLastFrame = false;

    // -1 means "use config value"
    private int   runtimeOperation = -1;
    private float runtimeGridSize  = -1f;

    private float modelScale = 1f;

    public ImguiGizmoScreen(String id) {
        this.id     = id;
        this.config = ImguiGizmoConfig.universal().build();
        this.theme  = ImguiGizmoTheme.dark().build();
        this.camera = new ImguiGizmoCamera();
    }

    public ImguiGizmoScreen setConfig(ImguiGizmoConfig cfg)    { this.config  = cfg; return this; }
    public ImguiGizmoScreen setTheme(ImguiGizmoTheme t)        { this.theme   = t;   return this; }
    public ImguiGizmoScreen setCamera(ImguiGizmoCamera cam)    { this.camera  = cam; return this; }
    public ImguiGizmoScreen setMiniBar(ImguiGizmoMiniBar bar)  { this.miniBar = bar; return this; }
    public ImguiGizmoScreen setSize(float w, float h)     { width = w; height = h; return this; }
    public ImguiGizmoScreen setTextureId(int id)          { this.textureId    = id;   return this; }
    public ImguiGizmoScreen setFlipTextureY(boolean flip) { this.flipTextureY = flip; return this; }
    public ImguiGizmoScreen setRuntimeOperation(int op)   { this.runtimeOperation = op;   return this; }
    public ImguiGizmoScreen setRuntimeGridSize(float s)   { this.runtimeGridSize  = s;    return this; }
    public ImguiGizmoScreen setModelScale(float scale)    { this.modelScale = Math.max(0.0001f, scale); return this; }

    public ImguiGizmoScreen setModelMatrix(Matrix4f m) {
        ImguiGizmoMatrices.fromMatrix4f(m, matrices.modelRaw());
        matrices.sync();
        return this;
    }

    public void render() {
        ImVec2 avail = ImGui.getContentRegionAvail();
        float w = (width  > 0f) ? width  : avail.x;
        float h = (height > 0f) ? height : avail.y;
        if (w < 1f) w = 1f;
        if (h < 1f) h = 1f;

        ImguiGizmoTheme.pushColor(ImGuiCol.ChildBg, theme.backgroundArgb);
        ImguiGizmoTheme.pushColor(ImGuiCol.Border,  theme.borderArgb);
        ImGui.pushStyleVar(ImGuiStyleVar.ChildRounding,   theme.borderRounding);
        ImGui.pushStyleVar(ImGuiStyleVar.ChildBorderSize, theme.borderThickness);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding,   0f, 0f);

        boolean childOk = ImGui.beginChild("##ImguiGizmoScreen_" + id, w, h, true,
                ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        ImGui.popStyleVar(3);
        ImGui.popStyleColor(2);

        if (!childOk) { ImGui.endChild(); return; }

        ImVec2 winPos  = ImGui.getWindowPos();
        ImVec2 winSize = ImGui.getWindowSize();
        float wx = winPos.x,  wy = winPos.y;
        float ww = winSize.x, wh = winSize.y;

        matrices.updateFromCamera(camera, ww, wh);

        boolean isOrtho = config.orthographic || camera.isOrthographic();
        ImguiGizmoMatrices.Context ctx = matrices.beginContext(isOrtho, wx, wy, ww, wh);

        float gs = (runtimeGridSize > 0f) ? runtimeGridSize : config.gridSize;

        if (config.showGrid) {
            ctx.drawGrid(gs);
        }

        if (textureId > 0) {
            float u0 = 0f, v0 = flipTextureY ? 1f : 0f;
            float u1 = 1f, v1 = flipTextureY ? 0f : 1f;
            ImGui.setCursorPos(0f, 0f);
            ImGui.image(textureId, ww, wh, u0, v0, u1, v1);
            // re-anchor so the manipulator draws on top of the image
            ctx.reanchor();
        }

        int operation = resolveOperation();
        boolean skip  = config.lockTransform || operation < 0;

        if (!skip) {
            float[] snap = buildSnapArray(operation);
            wasUsedLastFrame = ctx.drawManipulator(
                    operation, config.mode, snap, config.localBounds, config.boundsSnap);
        } else {
            wasUsedLastFrame = false;
        }

        camera.handleInput(config.lockView);

        if (!config.lockView) {
            boolean cubeChanged = ctx.drawViewCubeTopRight(
                    theme.viewCubeSize, config.viewCubeArmLength, theme.viewCubeBackground);
            if (cubeChanged) {
                ImguiGizmoMatrices.toMatrix4f(matrices.viewRaw(), afterView);
                camera.syncFromViewMatrix(afterView);
            }
        }

        if (miniBar != null) {
            miniBar.render(this, wx, wy, ww, wh);
        }

        matrices.sync();
        applyModelScale();
        ImGui.endChild();
    }

    // priority: mini-bar → runtimeOperation → config
    private int resolveOperation() {
        if (miniBar != null && miniBar.getSelectedOperation() >= 0) {
            return miniBar.getSelectedOperation();
        }
        return (runtimeOperation >= 0) ? runtimeOperation : config.operation;
    }

    private float[] buildSnapArray(int operation) {
        boolean isT = (operation & Operation.TRANSLATE) != 0;
        boolean isR = (operation & Operation.ROTATE)    != 0;
        boolean isS = (operation & Operation.SCALE)     != 0
                || (operation & Operation.SCALEU)    != 0;
        if (isT && config.snapTranslation)
            return new float[]{config.snapTranslationStep, config.snapTranslationStep, config.snapTranslationStep};
        if (isR && config.snapRotation)
            return new float[]{config.snapRotationDeg, config.snapRotationDeg, config.snapRotationDeg};
        if (isS && config.snapScale)
            return new float[]{config.snapScaleStep, config.snapScaleStep, config.snapScaleStep};
        return null;
    }

    /**
     * Re-normalises the rotation columns and re-applies modelScale, preserving
     * whatever rotation the gizmo has accumulated. Called at the end of render().
     */
    private void applyModelScale() {
        if (modelScale == 1f) return;
        float[] raw = matrices.modelRaw();
        float sx = colLen(raw, 0);
        float sy = colLen(raw, 4);
        float sz = colLen(raw, 8);
        if (sx < 0.0001f || sy < 0.0001f || sz < 0.0001f) return;
        float rx = modelScale / sx;
        float ry = modelScale / sy;
        float rz = modelScale / sz;
        raw[0] *= rx; raw[1] *= rx; raw[2] *= rx;
        raw[4] *= ry; raw[5] *= ry; raw[6] *= ry;
        raw[8] *= rz; raw[9] *= rz; raw[10] *= rz;
        matrices.sync();
    }

    private static float colLen(float[] m, int off) {
        float a = m[off], b = m[off + 1], c = m[off + 2];
        return (float) Math.sqrt(a * a + b * b + c * c);
    }

    public ImguiGizmoMatrices getMatrices()     { return matrices; }
    public boolean       wasImguiGizmoUsed()    { return wasUsedLastFrame; }
    public ImguiGizmoCamera   getCamera()       { return camera; }
    public ImguiGizmoConfig   getConfig()       { return config; }
    public ImguiGizmoTheme    getTheme()        { return theme; }
    public ImguiGizmoMiniBar  getMiniBar()      { return miniBar; }
    public String        getId()           { return id; }
    public float         getModelScale()   { return modelScale; }
}