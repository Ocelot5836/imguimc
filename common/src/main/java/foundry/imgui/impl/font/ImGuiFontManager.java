package foundry.imgui.impl.font;

import imgui.ImFont;
import imgui.ImFontAtlas;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NativeResource;

public interface ImGuiFontManager extends PreparableReloadListener, NativeResource {

    short[] DEFAULT_FONT_RANGES = new short[]{0x0020, 0x00FF, 0};
    float DEFAULT_FONT_SIZE = 20.0F;

    ImFont getFont(@Nullable Identifier name, boolean bold, boolean italic);

    void rebuildFonts(ImFontAtlas atlas);
}
