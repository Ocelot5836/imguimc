package foundry.imgui.api.text.editor;

// A single character and its syntax-highlight colour (ImGui ABGR format).
public final class EditorGlyph {
    public char ch;
    public int  color;

    public EditorGlyph(char ch, int color) {
        this.ch    = ch;
        this.color = color;
    }

    public EditorGlyph copy() {
        return new EditorGlyph(ch, color);
    }
}