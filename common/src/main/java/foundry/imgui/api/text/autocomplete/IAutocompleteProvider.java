package foundry.imgui.api.text.autocomplete;

import net.soul.shade.impl.client.editor.text.editor.EditorAutocomplete;
import net.soul.shade.impl.client.editor.text.editor.EditorCoordinates;
import net.soul.shade.impl.client.editor.text.editor.EditorGlyph;

import java.util.List;

/**
 * Language-specific autocomplete source. Implement for each language and pass into
 * {@link EditorAutocomplete}.
 */
public interface IAutocompleteProvider {

    // Called on every keypress — keep it fast.
    List<AutocompleteItem> getCandidates(String prefix,
                                         List<List<EditorGlyph>> lines,
                                         EditorCoordinates cursor);

    // Return true to hide the popup (e.g. GLSL after a dot for swizzles).
    boolean shouldSuppress(String prefix,
                           List<List<EditorGlyph>> lines,
                           EditorCoordinates cursor);

    default int minPrefixLength() { return 2; }

    // If true, accepted function completions get "()" appended.
    default boolean appendParens() { return true; }
}