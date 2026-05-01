package foundry.imgui.api.text.autocomplete;

public final class AutocompleteItem {
    public final String text;
    public final String type;      // "keyword", "type", "function", "variable", "constant"
    public final String signature;
    public final int    color;     // ABGR

    public AutocompleteItem(String text, String type, String signature, int color) {
        this.text      = text;
        this.type      = type;
        this.signature = signature;
        this.color     = color;
    }
}