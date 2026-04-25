package foundry.imgui.impl.font.v1;

//? if >=1.21.11 {

/*import com.mojang.serialization.Codec;
import foundry.imgui.api.ImGuiMC;
import foundry.imgui.impl.font.ImGuiFontManager;
import net.minecraft.server.packs.metadata.MetadataSectionType;

public record ImGuiFontSizeSectionSerializer(float size) {

    public static final Codec<ImGuiFontSizeSectionSerializer> CODEC = Codec.FLOAT.optionalFieldOf("size", ImGuiFontManager.DEFAULT_FONT_SIZE)
            .xmap(ImGuiFontSizeSectionSerializer::new, ImGuiFontSizeSectionSerializer::size)
            .codec();
    public static final MetadataSectionType<ImGuiFontSizeSectionSerializer> TYPE = new MetadataSectionType<>(ImGuiMC.MOD_ID + ":font_size", CODEC);

}
*///?}