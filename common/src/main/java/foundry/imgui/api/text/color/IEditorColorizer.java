package foundry.imgui.api.text.color;

import net.soul.shade.impl.client.editor.text.editor.EditorGlyph;

import java.util.List;

/**
 * Syntax highlighting contract. Only colorizeVisibleLines is called every frame,
 * so implementations should cache aggressively.
 */
public interface IEditorColorizer {

    void colorizeVisibleLines(List<List<EditorGlyph>> lines,
                              int firstVisibleLine, int lastVisibleLine);

    void markLineDirty(int lineIdx);

    void markLinesDirty(int startLine, int endLine);

    // Called after setText — nuke everything.
    void invalidateAll();

    // Immediate single-line recolor, used after autocomplete inserts text.
    void colorizeLine(List<List<EditorGlyph>> lines, int lineIdx);

    int getDefaultColor();
}