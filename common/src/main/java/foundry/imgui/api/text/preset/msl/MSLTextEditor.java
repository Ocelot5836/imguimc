package foundry.imgui.api.text.preset.msl;

import net.soul.shade.impl.client.editor.text.ImguiCoreTextEditor;
import net.soul.shade.impl.client.editor.text.editor.EditorTheme;

public final class MSLTextEditor {

    private final MSLColorizer            colorizer;
    private final MSLAutocompleteProvider provider;
    private final ImguiCoreTextEditor core;

    public MSLTextEditor() {
        this(EditorTheme.dark().build());
    }

    public MSLTextEditor(EditorTheme theme) {
        colorizer = new MSLColorizer();
        provider  = new MSLAutocompleteProvider(colorizer);
        core      = new ImguiCoreTextEditor(colorizer, provider, theme);
    }

    public void render(String id, float width, float height, boolean isResizing) {
        core.render(id, width, height, isResizing);
    }

    public void setText(String text)      { core.setText(text); }
    public String getText()               { return core.getText(); }
    public int    getTotalLines()         { return core.getTotalLines(); }
    public void   setReadOnly(boolean ro) { core.setReadOnly(ro); }
    public boolean isReadOnly()           { return core.isReadOnly(); }

    public ImguiCoreTextEditor getCore()       { return core; }
    public MSLColorizer   getColorizer()  { return colorizer; }
}