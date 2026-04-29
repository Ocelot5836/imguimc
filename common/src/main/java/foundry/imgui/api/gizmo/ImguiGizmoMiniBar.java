package foundry.imgui.api.gizmo;

import imgui.ImGui;
import imgui.extension.imguizmo.flag.Operation;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;

import java.util.HashSet;
import java.util.Set;

/**
 * A compact floating overlay inside a ImguiImguiGizmoScreen that lets the user switch
 * between gizmo operations with a single click.
 */
public final class ImguiGizmoMiniBar {

    public enum Anchor {
        TOP_LEFT,
        BOTTOM_LEFT
    }

    private Anchor  anchor          = Anchor.BOTTOM_LEFT;
    private float   marginX         = 8f;
    private float   marginY         = 8f;
    private float   buttonW         = 26f;
    private float   buttonH         = 22f;
    private float   spacing         = 2f;

    private boolean showTranslate   = true;
    private boolean showRotate      = true;
    private boolean showScale       = true;
    private boolean showUniversal   = false;
    private boolean showBounds      = false;

    private final Set<Integer> disabledOps = new HashSet<>();

    private int selectedOperation   = Operation.TRANSLATE;

    public ImguiGizmoMiniBar() {}

    public ImguiGizmoMiniBar(Anchor anchor) {
        this.anchor = anchor;
    }

    public ImguiGizmoMiniBar setAnchor(Anchor a)            { this.anchor        = a;  return this; }
    public ImguiGizmoMiniBar setMargin(float x, float y)    { marginX = x; marginY = y; return this; }
    public ImguiGizmoMiniBar setButtonSize(float w, float h){ buttonW = w; buttonH = h; return this; }
    public ImguiGizmoMiniBar setSpacing(float s)            { this.spacing       = s;  return this; }
    public ImguiGizmoMiniBar showTranslate(boolean v)       { this.showTranslate = v;  return this; }
    public ImguiGizmoMiniBar showRotate(boolean v)          { this.showRotate    = v;  return this; }
    public ImguiGizmoMiniBar showScale(boolean v)           { this.showScale     = v;  return this; }
    public ImguiGizmoMiniBar showUniversal(boolean v)       { this.showUniversal = v;  return this; }
    public ImguiGizmoMiniBar showBounds(boolean v)          { this.showBounds    = v;  return this; }
    public ImguiGizmoMiniBar setOperation(int op)           { selectedOperation  = op; return this; }

    /**
     * Hides the button for the given operation. If the currently selected op
     * is disabled, the selection is cleared.
     */
    public ImguiGizmoMiniBar setEnabled(int op, boolean enabled) {
        if (enabled) disabledOps.remove(op);
        else         disabledOps.add(op);
        if (!enabled && selectedOperation == op) selectedOperation = -1;
        return this;
    }

    /**
     * Shows only the given operations and hides all others. Picks the first
     * supplied op as selected if the current selection isn't among them.
     */
    public ImguiGizmoMiniBar enableOnly(int... ops) {
        Set<Integer> allowed = new HashSet<>();
        for (int op : ops) allowed.add(op);
        showTranslate = allowed.contains(Operation.TRANSLATE);
        showRotate    = allowed.contains(Operation.ROTATE);
        showScale     = allowed.contains(Operation.SCALE) || allowed.contains(Operation.SCALEU);
        showUniversal = allowed.contains(Operation.UNIVERSAL);
        showBounds    = allowed.contains(Operation.BOUNDS);
        disabledOps.clear();
        if (!allowed.contains(selectedOperation) && !allowed.isEmpty()) {
            selectedOperation = ops[0];
        }
        return this;
    }

    void render(ImguiGizmoScreen screen, float wx, float wy, float ww, float wh) {
        renderImpl(wx, wy, ww, wh, screen.getId() + "_minibar", screen.getTheme());
    }

    public void renderStandalone(float wx, float wy, float ww, float wh) {
        renderImpl(wx, wy, ww, wh, "gizmo_minibar_standalone", ImguiGizmoTheme.dark().build());
    }

    public void renderStandalone(float wx, float wy, float ww, float wh, ImguiGizmoTheme theme) {
        renderImpl(wx, wy, ww, wh, "gizmo_minibar_standalone", theme);
    }

    private void renderImpl(float wx, float wy, float ww, float wh, String windowId, ImguiGizmoTheme theme) {
        int count = 0;
        if (showTranslate && !disabledOps.contains(Operation.TRANSLATE)) count++;
        if (showRotate    && !disabledOps.contains(Operation.ROTATE))    count++;
        if (showScale     && !disabledOps.contains(Operation.SCALE)
                && !disabledOps.contains(Operation.SCALEU))    count++;
        if (showUniversal && !disabledOps.contains(Operation.UNIVERSAL)) count++;
        if (showBounds    && !disabledOps.contains(Operation.BOUNDS))    count++;
        if (count == 0)    return;

        float barW = count * (buttonW + spacing) - spacing + 8f;
        float barH = buttonH + 8f;

        float posX, posY;
        if (anchor == Anchor.TOP_LEFT) {
            posX = wx + marginX;
            posY = wy + marginY;
        } else {
            posX = wx + marginX;
            posY = wy + wh - barH - marginY;
        }

        ImGui.setNextWindowPos(posX, posY);
        ImGui.setNextWindowSize(barW, barH);

        float themeBgAlpha = ((theme.miniBarBgArgb >>> 24) & 0xFF) / 255f;
        ImGui.setNextWindowBgAlpha(themeBgAlpha);

        int childFlags = ImGuiWindowFlags.NoTitleBar
                | ImGuiWindowFlags.NoResize
                | ImGuiWindowFlags.NoScrollbar
                | ImGuiWindowFlags.NoMove
                | ImGuiWindowFlags.NoSavedSettings;

        float r = theme.miniBarRounding;
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding,  r);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding,   4f, 4f);
        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing,     spacing, 0f);
        ImguiGizmoTheme.pushColor(ImGuiCol.WindowBg, theme.miniBarBgArgb);
        ImguiGizmoTheme.pushColor(ImGuiCol.Border,   theme.miniBarBorderArgb);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 1f);

        if (ImGui.begin("##" + windowId, childFlags)) {

            boolean first = true;

            if (showTranslate && !disabledOps.contains(Operation.TRANSLATE)) {
                if (!first) ImGui.sameLine(0, spacing);
                drawOpButton("P", Operation.TRANSLATE, false, theme);
                first = false;
            }
            if (showRotate && !disabledOps.contains(Operation.ROTATE)) {
                if (!first) ImGui.sameLine(0, spacing);
                drawOpButton("R", Operation.ROTATE, false, theme);
                first = false;
            }
            if (showScale && !disabledOps.contains(Operation.SCALE)
                    && !disabledOps.contains(Operation.SCALEU)) {
                if (!first) ImGui.sameLine(0, spacing);
                drawOpButton("S", Operation.SCALE, false, theme);
                first = false;
            }
            if (showUniversal && !disabledOps.contains(Operation.UNIVERSAL)) {
                if (!first) ImGui.sameLine(0, spacing);
                drawOpButton("U", Operation.UNIVERSAL, false, theme);
                first = false;
            }
            if (showBounds && !disabledOps.contains(Operation.BOUNDS)) {
                if (!first) ImGui.sameLine(0, spacing);
                drawOpButton("B", Operation.BOUNDS, false, theme);
            }
        }
        ImGui.end();

        ImGui.popStyleVar(4);
        ImGui.popStyleColor(2);
    }

    private void drawOpButton(String label, int op, boolean isLast, ImguiGizmoTheme theme) {
        boolean active = (selectedOperation == op);

        if (active) {
            ImguiGizmoTheme.pushColor(ImGuiCol.Button,        theme.miniBarButtonActiveArgb);
            ImguiGizmoTheme.pushColor(ImGuiCol.ButtonHovered, theme.miniBarButtonActiveHoverArgb);
            ImguiGizmoTheme.pushColor(ImGuiCol.ButtonActive,  dimArgb(theme.miniBarButtonActiveArgb, 0.80f));
            ImguiGizmoTheme.pushColor(ImGuiCol.Text,          theme.miniBarTextActiveArgb);
        } else {
            ImguiGizmoTheme.pushColor(ImGuiCol.Button,        theme.miniBarButtonInactiveArgb);
            ImguiGizmoTheme.pushColor(ImGuiCol.ButtonHovered, theme.miniBarButtonInactiveHoverArgb);
            ImguiGizmoTheme.pushColor(ImGuiCol.ButtonActive,  dimArgb(theme.miniBarButtonInactiveArgb, 0.80f));
            ImguiGizmoTheme.pushColor(ImGuiCol.Text,          theme.miniBarTextInactiveArgb);
        }

        ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, theme.miniBarRounding - 1f);
        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding,  0f, 0f);

        if (ImGui.button(label + "##minibar_" + op, buttonW, buttonH)) {
            selectedOperation = op;
        }

        ImGui.popStyleVar(2);
        ImGui.popStyleColor(4);

        if (ImGui.isItemHovered()) {
            ImGui.setTooltip(opTooltip(op));
        }
    }

    private static int dimArgb(int abgr, float factor) {
        int a = (abgr >>> 24) & 0xFF;
        int b = Math.min(255, (int)(((abgr >>> 16) & 0xFF) * factor));
        int g = Math.min(255, (int)(((abgr >>>  8) & 0xFF) * factor));
        int r = Math.min(255, (int)(( abgr         & 0xFF) * factor));
        return (a << 24) | (b << 16) | (g << 8) | r;
    }

    private static String opTooltip(int op) {
        if (op == Operation.TRANSLATE) return "Translate  [P]";
        if (op == Operation.ROTATE)    return "Rotate     [R]";
        if (op == Operation.SCALE)     return "Scale      [S]";
        if (op == Operation.UNIVERSAL) return "Universal  [U]";
        if (op == Operation.BOUNDS)    return "Bounds     [B]";
        return "Operation";
    }

    public int     getSelectedOperation() { return selectedOperation; }
    public Anchor  getAnchor()            { return anchor; }
    public boolean isShowTranslate()      { return showTranslate; }
    public boolean isShowRotate()         { return showRotate; }
    public boolean isShowScale()          { return showScale; }
    public boolean isShowUniversal()      { return showUniversal; }
    public boolean isShowBounds()         { return showBounds; }
}