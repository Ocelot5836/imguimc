package foundry.imgui.api.gizmo;

/**
 * All visual properties for a ImguiGizmoScreen.
 * Colors are ImGui ABGR packed ints (0xAABBGGRR).
 */
public final class ImguiGizmoTheme {

    public final int backgroundArgb;

    public final int borderArgb;
    public final float borderThickness;
    public final float borderRounding;

    // NOTE: gridColor can't be pushed at runtime — SpaiR binding doesn't expose ImGuizmoStyle.
    // Stored here for serialisation / future use if the binding is upgraded.
    public final int gridColor;
    public final float defaultGridSize;
    public final boolean gridVisible;

    // 0x00000000 = transparent
    public final int viewCubeBackground;
    public final float viewCubeSize;

    public final int overlayTextArgb;

    public final int   miniBarBgArgb;
    public final int   miniBarBorderArgb;
    public final float miniBarRounding;
    public final int   miniBarButtonActiveArgb;
    public final int   miniBarButtonActiveHoverArgb;
    public final int   miniBarButtonInactiveArgb;
    public final int   miniBarButtonInactiveHoverArgb;
    public final int   miniBarTextActiveArgb;
    public final int   miniBarTextInactiveArgb;

    // Same note as gridColor — these can't be applied until the binding exposes ImGuizmo::GetStyle().
    public final float translationLineThickness;
    public final float translationLineArrowSize;
    public final float rotationLineThickness;
    public final float rotationOuterLineThickness;
    public final float scaleLineThickness;
    public final float scaleLineCircleSize;

    private ImguiGizmoTheme(Builder b) {
        this.backgroundArgb          = b.backgroundArgb;
        this.borderArgb              = b.borderArgb;
        this.borderThickness         = b.borderThickness;
        this.borderRounding          = b.borderRounding;
        this.gridColor               = b.gridColor;
        this.defaultGridSize         = b.defaultGridSize;
        this.gridVisible             = b.gridVisible;
        this.viewCubeBackground      = b.viewCubeBackground;
        this.viewCubeSize            = b.viewCubeSize;
        this.overlayTextArgb         = b.overlayTextArgb;
        this.miniBarBgArgb                    = b.miniBarBgArgb;
        this.miniBarBorderArgb                = b.miniBarBorderArgb;
        this.miniBarRounding                  = b.miniBarRounding;
        this.miniBarButtonActiveArgb          = b.miniBarButtonActiveArgb;
        this.miniBarButtonActiveHoverArgb     = b.miniBarButtonActiveHoverArgb;
        this.miniBarButtonInactiveArgb        = b.miniBarButtonInactiveArgb;
        this.miniBarButtonInactiveHoverArgb   = b.miniBarButtonInactiveHoverArgb;
        this.miniBarTextActiveArgb            = b.miniBarTextActiveArgb;
        this.miniBarTextInactiveArgb          = b.miniBarTextInactiveArgb;
        this.translationLineThickness     = b.translationLineThickness;
        this.translationLineArrowSize     = b.translationLineArrowSize;
        this.rotationLineThickness        = b.rotationLineThickness;
        this.rotationOuterLineThickness   = b.rotationOuterLineThickness;
        this.scaleLineThickness           = b.scaleLineThickness;
        this.scaleLineCircleSize          = b.scaleLineCircleSize;
    }

    public static Builder dark() {
        return new Builder()
                .withBackgroundArgb(0xE6120D0A)
                .withBorderArgb(0xCC7A5020)
                .withBorderThickness(1.5f)
                .withBorderRounding(5f)
                .withGridColor(0xFF443830)
                .withDefaultGridSize(1f)
                .withGridVisible(true)
                .withViewCubeBackground(0x00000000)
                .withViewCubeSize(80f)
                .withOverlayTextArgb(0xFF998880)
                .withMiniBarBgArgb(0xD1100C09)
                .withMiniBarBorderArgb(0xCC503820)
                .withMiniBarRounding(4f)
                .withMiniBarButtonActiveArgb(0xF2622810)
                .withMiniBarButtonActiveHoverArgb(0xF27A3818)
                .withMiniBarButtonInactiveArgb(0xE61A1008)
                .withMiniBarButtonInactiveHoverArgb(0xE6281A0E)
                .withMiniBarTextActiveArgb(0xFFF5E8D8)
                .withMiniBarTextInactiveArgb(0xFF806050)
                .withTranslationLineThickness(3f)
                .withTranslationLineArrowSize(6f)
                .withRotationLineThickness(2f)
                .withRotationOuterLineThickness(3f)
                .withScaleLineThickness(3f)
                .withScaleLineCircleSize(6f);
    }

    public static Builder warm() {
        return new Builder()
                .withBackgroundArgb(0xE6150E08)
                .withBorderArgb(0xCCA06030)
                .withBorderThickness(1.5f)
                .withBorderRounding(5f)
                .withGridColor(0xFF504030)
                .withDefaultGridSize(1f)
                .withGridVisible(true)
                .withViewCubeBackground(0x00000000)
                .withViewCubeSize(80f)
                .withOverlayTextArgb(0xFF998878)
                .withMiniBarBgArgb(0xE6302622)
                .withMiniBarBorderArgb(0xCCA09060)
                .withMiniBarRounding(5f)
                .withMiniBarButtonActiveArgb(0xF2306070)
                .withMiniBarButtonActiveHoverArgb(0xF2408090)
                .withMiniBarButtonInactiveArgb(0xE6403830)
                .withMiniBarButtonInactiveHoverArgb(0xE6504840)
                .withMiniBarTextActiveArgb(0xFF88C8D8)
                .withMiniBarTextInactiveArgb(0xFF907868)
                .withTranslationLineThickness(3f)
                .withTranslationLineArrowSize(6f)
                .withRotationLineThickness(2f)
                .withRotationOuterLineThickness(3f)
                .withScaleLineThickness(3f)
                .withScaleLineCircleSize(6f);
    }

    public ImguiGizmoTheme withBorderArgb(int c)                  { return toBuilder().withBorderArgb(c).build(); }
    public ImguiGizmoTheme withBorderThickness(float t)           { return toBuilder().withBorderThickness(t).build(); }
    public ImguiGizmoTheme withGridColor(int c)                   { return toBuilder().withGridColor(c).build(); }
    public ImguiGizmoTheme withGridSize(float s)                  { return toBuilder().withDefaultGridSize(s).build(); }
    public ImguiGizmoTheme withGridVisible(boolean v)             { return toBuilder().withGridVisible(v).build(); }
    public ImguiGizmoTheme withBackground(int c)                  { return toBuilder().withBackgroundArgb(c).build(); }
    public ImguiGizmoTheme withMiniBarBgArgb(int c)               { return toBuilder().withMiniBarBgArgb(c).build(); }
    public ImguiGizmoTheme withMiniBarBorderArgb(int c)           { return toBuilder().withMiniBarBorderArgb(c).build(); }
    public ImguiGizmoTheme withMiniBarRounding(float r)           { return toBuilder().withMiniBarRounding(r).build(); }
    public ImguiGizmoTheme withMiniBarButtonActiveArgb(int c)     { return toBuilder().withMiniBarButtonActiveArgb(c).build(); }
    public ImguiGizmoTheme withMiniBarButtonActiveHoverArgb(int c){ return toBuilder().withMiniBarButtonActiveHoverArgb(c).build(); }
    public ImguiGizmoTheme withMiniBarButtonInactiveArgb(int c)   { return toBuilder().withMiniBarButtonInactiveArgb(c).build(); }
    public ImguiGizmoTheme withMiniBarButtonInactiveHoverArgb(int c){ return toBuilder().withMiniBarButtonInactiveHoverArgb(c).build(); }
    public ImguiGizmoTheme withMiniBarTextActiveArgb(int c)       { return toBuilder().withMiniBarTextActiveArgb(c).build(); }
    public ImguiGizmoTheme withMiniBarTextInactiveArgb(int c)     { return toBuilder().withMiniBarTextInactiveArgb(c).build(); }
    public ImguiGizmoTheme withTranslationLineThickness(float v)  { return toBuilder().withTranslationLineThickness(v).build(); }
    public ImguiGizmoTheme withTranslationLineArrowSize(float v)  { return toBuilder().withTranslationLineArrowSize(v).build(); }
    public ImguiGizmoTheme withRotationLineThickness(float v)     { return toBuilder().withRotationLineThickness(v).build(); }
    public ImguiGizmoTheme withRotationOuterLineThickness(float v){ return toBuilder().withRotationOuterLineThickness(v).build(); }
    public ImguiGizmoTheme withScaleLineThickness(float v)        { return toBuilder().withScaleLineThickness(v).build(); }
    public ImguiGizmoTheme withScaleLineCircleSize(float v)       { return toBuilder().withScaleLineCircleSize(v).build(); }

    private Builder toBuilder() {
        Builder b = new Builder();
        b.backgroundArgb             = backgroundArgb;
        b.borderArgb                 = borderArgb;
        b.borderThickness            = borderThickness;
        b.borderRounding             = borderRounding;
        b.gridColor                  = gridColor;
        b.defaultGridSize            = defaultGridSize;
        b.gridVisible                = gridVisible;
        b.viewCubeBackground         = viewCubeBackground;
        b.viewCubeSize               = viewCubeSize;
        b.overlayTextArgb            = overlayTextArgb;
        b.miniBarBgArgb                  = miniBarBgArgb;
        b.miniBarBorderArgb              = miniBarBorderArgb;
        b.miniBarRounding                = miniBarRounding;
        b.miniBarButtonActiveArgb        = miniBarButtonActiveArgb;
        b.miniBarButtonActiveHoverArgb   = miniBarButtonActiveHoverArgb;
        b.miniBarButtonInactiveArgb      = miniBarButtonInactiveArgb;
        b.miniBarButtonInactiveHoverArgb = miniBarButtonInactiveHoverArgb;
        b.miniBarTextActiveArgb          = miniBarTextActiveArgb;
        b.miniBarTextInactiveArgb        = miniBarTextInactiveArgb;
        b.translationLineThickness   = translationLineThickness;
        b.translationLineArrowSize   = translationLineArrowSize;
        b.rotationLineThickness      = rotationLineThickness;
        b.rotationOuterLineThickness = rotationOuterLineThickness;
        b.scaleLineThickness         = scaleLineThickness;
        b.scaleLineCircleSize        = scaleLineCircleSize;
        return b;
    }

    public static final class Builder {
        private int   backgroundArgb    = 0xE6120D0A;
        private int   borderArgb        = 0xCC7A5020;
        private float borderThickness   = 1.5f;
        private float borderRounding    = 5f;
        private int   gridColor         = 0xFF443830;
        private float defaultGridSize   = 1f;
        private boolean gridVisible     = true;
        private int   viewCubeBackground = 0x00000000;
        private float viewCubeSize      = 80f;
        private int   overlayTextArgb   = 0xFF998880;
        private int   miniBarBgArgb                  = 0xD1100C09;
        private int   miniBarBorderArgb              = 0xCC503820;
        private float miniBarRounding                = 4f;
        private int   miniBarButtonActiveArgb        = 0xF2622810;
        private int   miniBarButtonActiveHoverArgb   = 0xF27A3818;
        private int   miniBarButtonInactiveArgb      = 0xE61A1008;
        private int   miniBarButtonInactiveHoverArgb = 0xE6281A0E;
        private int   miniBarTextActiveArgb          = 0xFFF5E8D8;
        private int   miniBarTextInactiveArgb        = 0xFF806050;
        private float translationLineThickness   = 3f;
        private float translationLineArrowSize   = 6f;
        private float rotationLineThickness      = 2f;
        private float rotationOuterLineThickness = 3f;
        private float scaleLineThickness         = 3f;
        private float scaleLineCircleSize        = 6f;

        public Builder withBackgroundArgb(int c)              { backgroundArgb             = c; return this; }
        public Builder withBorderArgb(int c)                  { borderArgb                 = c; return this; }
        public Builder withBorderThickness(float t)           { borderThickness            = t; return this; }
        public Builder withBorderRounding(float r)            { borderRounding             = r; return this; }
        public Builder withGridColor(int c)                   { gridColor                  = c; return this; }
        public Builder withDefaultGridSize(float s)           { defaultGridSize            = s; return this; }
        public Builder withGridVisible(boolean v)             { gridVisible                = v; return this; }
        public Builder withViewCubeBackground(int c)          { viewCubeBackground         = c; return this; }
        public Builder withViewCubeSize(float s)              { viewCubeSize               = s; return this; }
        public Builder withOverlayTextArgb(int c)             { overlayTextArgb            = c; return this; }
        public Builder withMiniBarBgArgb(int c)               { miniBarBgArgb                  = c; return this; }
        public Builder withMiniBarBorderArgb(int c)           { miniBarBorderArgb              = c; return this; }
        public Builder withMiniBarRounding(float r)           { miniBarRounding                = r; return this; }
        public Builder withMiniBarButtonActiveArgb(int c)     { miniBarButtonActiveArgb        = c; return this; }
        public Builder withMiniBarButtonActiveHoverArgb(int c){ miniBarButtonActiveHoverArgb   = c; return this; }
        public Builder withMiniBarButtonInactiveArgb(int c)   { miniBarButtonInactiveArgb      = c; return this; }
        public Builder withMiniBarButtonInactiveHoverArgb(int c){ miniBarButtonInactiveHoverArgb = c; return this; }
        public Builder withMiniBarTextActiveArgb(int c)       { miniBarTextActiveArgb          = c; return this; }
        public Builder withMiniBarTextInactiveArgb(int c)     { miniBarTextInactiveArgb        = c; return this; }
        public Builder withTranslationLineThickness(float v)  { translationLineThickness   = v; return this; }
        public Builder withTranslationLineArrowSize(float v)  { translationLineArrowSize   = v; return this; }
        public Builder withRotationLineThickness(float v)     { rotationLineThickness      = v; return this; }
        public Builder withRotationOuterLineThickness(float v){ rotationOuterLineThickness = v; return this; }
        public Builder withScaleLineThickness(float v)        { scaleLineThickness         = v; return this; }
        public Builder withScaleLineCircleSize(float v)       { scaleLineCircleSize        = v; return this; }

        public ImguiGizmoTheme build() { return new ImguiGizmoTheme(this); }
    }

    /** Unpacks a packed ABGR int and pushes the given ImGui color slot. */
    public static void pushColor(int slot, int abgr) {
        float a = ((abgr >>> 24) & 0xFF) / 255f;
        float b = ((abgr >>> 16) & 0xFF) / 255f;
        float g = ((abgr >>>  8) & 0xFF) / 255f;
        float r = ( abgr         & 0xFF) / 255f;
        imgui.ImGui.pushStyleColor(slot, r, g, b, a);
    }
}