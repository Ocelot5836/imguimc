# Text Editor

A code editor widget you can drop into any ImGui window. It handles syntax highlighting, autocomplete, undo/redo, mouse selection, and the usual keyboard shortcuts. The colorizer and autocomplete provider are separate interfaces, so you can swap them out or write your own without touching the editor itself.

---

## Preset editors

`ShaderTextEditor` covers GLSL, HLSL, and MSL out of the box. Just pick a `Language` — everything else is the same API.

<table>
<tr>
<td align="center" width="33%">
<b>Language.GLSL</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/63b512b5e9e2c760ce61fc484e61eaef08a0f90a/wiki/screenshots/texteditor/GLSL.png?raw=true" width="220"/>
</td>
<td align="center" width="33%">
<b>Language.HLSL</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/63b512b5e9e2c760ce61fc484e61eaef08a0f90a/wiki/screenshots/texteditor/HLSL.png?raw=true" width="220"/>
</td>
<td align="center" width="33%">
<b>Language.MSL</b><br/>
<img src="https://github.com/Manyaarvadiya/imguimc/blob/63b512b5e9e2c760ce61fc484e61eaef08a0f90a/wiki/screenshots/texteditor/MSL.png?raw=true" width="220"/>
</td>
</tr>
</table>

```java
// dark theme by default
ShaderTextEditor editor = new ShaderTextEditor(ShaderTextEditor.Language.GLSL);

// or pass a theme
ShaderTextEditor editor = new ShaderTextEditor(ShaderTextEditor.Language.HLSL, EditorTheme.monokai().build());

editor.setText(myShaderSource);

// call this every frame inside your ImGui draw callback
editor.render("##myEditor", width, height, false);

// read back whenever
String source = editor.getText();

editor.setReadOnly(true);
int lines = editor.getTotalLines();

// get at internals if you need them
ImguiCoreTextEditor core       = editor.getCore();
IEditorColorizer    colorizer  = editor.getColorizer();
ShaderTextEditor.Language lang = editor.getLanguage();
```

---

## Themes

All the built-in themes are static factory methods on `EditorTheme` that return a `Builder`. Chain any overrides you want, then call `.build()`.

```java
// grab a preset as-is
EditorTheme theme = EditorTheme.dracula().build();

// or tweak one
EditorTheme theme = EditorTheme.dark()
        .withCursorColor(0xFF00FFFF)
        .withTabSize(2)
        .withLineSpacing(1.2f)
        .withCursorBlinkMs(500)  // 0 turns blinking off entirely
        .build();

// or build from scratch
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

Colors are packed ABGR ints throughout.

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

## Custom language support

If you need a language that isn't covered by the presets, implement `IEditorColorizer` and `IAutocompleteProvider` and hand them directly to `ImguiCoreTextEditor`. The screenshot below is a custom colorizer written for an in-house scripting language called Shard.

![Custom Editor Example](https://github.com/Manyaarvadiya/imguimc/blob/63b512b5e9e2c760ce61fc484e61eaef08a0f90a/wiki/screenshots/texteditor/shard.png?raw=true)

### Colorizer

Extend `AbstractBaseColorizer` — it manages the per-line dirty cache for you, so you only need to implement two methods.

```java
public class MyColorizer extends AbstractBaseColorizer {

    public static final int COLOR_DEFAULT = 0xFFD4D4D4;
    public static final int COLOR_KEYWORD = 0xFF569CD6;
    public static final int COLOR_COMMENT = 0xFF6A9955;
    public static final int COLOR_NUMBER  = 0xFFB5CEA8;

    private static final Set<String> KEYWORDS = Set.of("if", "else", "for", "while", "return");

    @Override
    public int getDefaultColor() { return COLOR_DEFAULT; }

    // runs once after setText / invalidateAll — good place to scan for user-defined types/functions
    @Override
    protected void analyzeDocument(List<List<EditorGlyph>> lines) { }

    // called for each visible line; set glyph.color on every character
    @Override
    protected void colorizeLineImpl(List<EditorGlyph> line, int lineIdx, String text) {
        int commentAt = findLineCommentStart(text); // finds the first // not inside a string
        String code = commentAt >= 0 ? text.substring(0, commentAt) : text;

        // tokenize 'code' and assign colors ...

        if (commentAt >= 0)
            for (int i = commentAt; i < line.size(); i++)
                line.get(i).color = COLOR_COMMENT;
    }
}
```

`glyphsToString(line)` turns a glyph list back into a plain String. `findLineCommentStart(text)` gives you the index of the first `//` that isn't inside a string literal, or -1 if there isn't one.

### Autocomplete provider

```java
public class MyAutocompleteProvider implements IAutocompleteProvider {

    @Override public int     minPrefixLength() { return 2; }
    @Override public boolean appendParens()    { return true; } // appends "()" when accepting a function

    @Override
    public boolean shouldSuppress(String prefix, List<List<EditorGlyph>> lines, EditorCoordinates cursor) {
        // suppress after a dot so swizzles and member access don't trigger the popup
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

        // valid type values: "keyword" | "type" | "function" | "variable" | "constant" | "qualifier"
        return out;
    }
}
```

To show a proper signature in the popup, use `FunctionSignature`:

```java
String sig = new FunctionSignature("clamp", "float", "float x, float minVal, float maxVal").format();
// → "float clamp(float x, float minVal, float maxVal)"
```

### Putting it together

```java
MyColorizer colorizer     = new MyColorizer();
MyAutocompleteProvider ac = new MyAutocompleteProvider(); // pass null to disable autocomplete
EditorTheme theme         = EditorTheme.dark().build();

ImguiCoreTextEditor editor = new ImguiCoreTextEditor(colorizer, ac, theme);
editor.setText("// your content here");

// every frame inside your ImGui callback
// pass isResizing = true while the user drags a resize handle — it suspends text input
editor.render("##myEditor", availW, availH, isResizing);
```

---

## Keyboard shortcuts

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

Double-click selects a word, triple-click selects the whole line. The undo stack caps at 200 entries. If you're making programmatic changes and want them to be undoable, call `editor.pushUndo()` first.
