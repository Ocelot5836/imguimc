package foundry.imgui.api.event;

import foundry.imgui.api.ImGuiMC;
import imgui.ImFont;
import imgui.ImFontAtlas;

/**
 * Fired when the font atlas is rebuilt before the next ImGui frame.
 *
 * @see ImGuiMC#rebuildFonts()
 * @since 1.0.0
 */
public interface RegisterImGuiFontsEvent {

    void registerImGuiFonts(ImFontAtlas atlas, ImFont defaultFont, float fontScale);
}
