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

*Coming soon.*
