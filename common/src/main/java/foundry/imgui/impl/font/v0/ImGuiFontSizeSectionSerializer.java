package foundry.imgui.impl.font.v0;

//? if <1.21.4 {

import com.google.gson.JsonObject;
import foundry.imgui.api.ImGuiMC;
import foundry.imgui.impl.font.ImGuiFontManager;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class ImGuiFontSizeSectionSerializer implements MetadataSectionSerializer<Float> {

    public static final ImGuiFontSizeSectionSerializer INSTANCE = new ImGuiFontSizeSectionSerializer();

    @Override
    public @NotNull String getMetadataSectionName() {
        return ImGuiMC.MOD_ID + ":font_size";
    }

    @Override
    public @NotNull Float fromJson(final JsonObject json) {
        if (!json.has("size")) {
            return ImGuiFontManager.DEFAULT_FONT_SIZE;
        }

        return GsonHelper.getAsFloat(json, "size", ImGuiFontManager.DEFAULT_FONT_SIZE);
    }
}
//?}