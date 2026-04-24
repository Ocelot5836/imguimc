package foundry.imgui.impl.font.v0;

//? if <1.21.6 {

/*import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import foundry.imgui.api.ImGuiMC;
import foundry.imgui.impl.font.ImGuiFontManager;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class ImGuiFontRangesSectionSerializer implements MetadataSectionSerializer<ImGuiFontRangesSectionSerializer.FontMetadata> {

    public static final ImGuiFontRangesSectionSerializer INSTANCE = new ImGuiFontRangesSectionSerializer();

    @Override
    public @NotNull String getMetadataSectionName() {
        return ImGuiMC.MOD_ID + ":font_ranges";
    }

    @Override
    public @NotNull FontMetadata fromJson(final JsonObject json) {
        if (!json.has("ranges")) {
            return new FontMetadata(ImGuiFontManager.DEFAULT_FONT_RANGES);
        }

        final JsonArray rangesJson = GsonHelper.getAsJsonArray(json, "ranges");
        final ShortList ranges = new ShortArrayList(rangesJson.size() * 2 + 3);
        ranges.add((short) 0x0020);
        ranges.add((short) 0x00FF);

        for (int i = 0; i < rangesJson.size(); i++) {
            final JsonElement element = rangesJson.get(i);
            if (element.isJsonPrimitive()) {
                final String builtInRange = GsonHelper.convertToString(element, "ranges[" + i + "]").toLowerCase(Locale.ROOT);
                switch (builtInRange) {
                    case "greek" -> {
                        // Greek and Coptic
                        ranges.add((short) 0x0370);
                        ranges.add((short) 0x03FF);
                    }
                    case "korean" -> {
                        // Korean alphabets
                        ranges.add((short) 0x3131);
                        ranges.add((short) 0x3163);
                        // Korean characters
                        ranges.add((short) 0xAC00);
                        ranges.add((short) 0xD7A3);
                    }
                    case "japanese" -> {
                        // CJK Symbols and Punctuations, Hiragana, Katakana
                        ranges.add((short) 0x3000);
                        ranges.add((short) 0x30FF);
                        // Katakana Phonetic Extensions
                        ranges.add((short) 0x31F0);
                        ranges.add((short) 0x31FF);
                        // Half-width characters
                        ranges.add((short) 0xFF00);
                        ranges.add((short) 0xFFEF);
                    }
                    case "chinese" -> {
                        // General Punctuation
                        ranges.add((short) 0x2000);
                        ranges.add((short) 0x206F);
                        // CJK Symbols and Punctuations, Hiragana, Katakana
                        ranges.add((short) 0x3000);
                        ranges.add((short) 0x30FF);
                        // Katakana Phonetic Extensions
                        ranges.add((short) 0x31F0);
                        ranges.add((short) 0x31FF);
                        // Half-width characters
                        ranges.add((short) 0xFF00);
                        ranges.add((short) 0xFFEF);
                        // CJK Ideograms
                        ranges.add((short) 0x4e00);
                        ranges.add((short) 0x9FAF);
                    }
                    case "cyrillic" -> {
                        // Cyrillic + Cyrillic Supplement
                        ranges.add((short) 0x0400);
                        ranges.add((short) 0x052F);
                        // Cyrillic Extended-A
                        ranges.add((short) 0x2DE0);
                        ranges.add((short) 0x2DFF);
                        // Cyrillic Extended-B
                        ranges.add((short) 0xA640);
                        ranges.add((short) 0xA69F);
                    }
                    case "thai" -> {
                        // Punctuations
                        ranges.add((short) 0x2010);
                        ranges.add((short) 0x205E);
                        // Thai
                        ranges.add((short) 0x0E00);
                        ranges.add((short) 0x0E7F);
                    }
                    case "vietnamese" -> {
                        ranges.add((short) 0x0102);
                        ranges.add((short) 0x0103);
                        ranges.add((short) 0x0110);
                        ranges.add((short) 0x0111);
                        ranges.add((short) 0x0128);
                        ranges.add((short) 0x0129);
                        ranges.add((short) 0x0168);
                        ranges.add((short) 0x0169);
                        ranges.add((short) 0x01A0);
                        ranges.add((short) 0x01A1);
                        ranges.add((short) 0x01AF);
                        ranges.add((short) 0x01B0);
                        ranges.add((short) 0x1EA0);
                        ranges.add((short) 0x1EF9);
                    }
                    default -> throw new JsonSyntaxException("Unknown built-in range: " + builtInRange);
                }
            } else if (element.isJsonObject()) {
                final JsonObject object = element.getAsJsonObject();
                final int min = GsonHelper.getAsInt(object, "min");
                final int max = GsonHelper.getAsInt(object, "max");
                ranges.add((short) min);
                ranges.add((short) max);
            } else {
                throw new JsonSyntaxException("Expected ranges[" + i + "] to be a string or JsonElement, was " + GsonHelper.getType(element));
            }
        }
        ranges.add((short) 0);
        return new FontMetadata(ranges.toShortArray());
    }

    public record FontMetadata(short[] ranges) {
    }
}
*///?}