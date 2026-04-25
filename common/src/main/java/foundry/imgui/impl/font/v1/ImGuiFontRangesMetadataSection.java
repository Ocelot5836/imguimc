package foundry.imgui.impl.font.v1;

//? if >=1.21.4 {

/*import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import foundry.imgui.api.ImGuiMC;
import foundry.imgui.impl.font.ImGuiFontManager;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import net.minecraft.server.packs.metadata.MetadataSectionType;

import java.util.Locale;
import java.util.stream.Stream;

public record ImGuiFontRangesMetadataSection(short[] ranges) {

    public static final Codec<ImGuiFontRangesMetadataSection> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            new Codec<short[]>() {
                @Override
                public <T> DataResult<T> encode(final short[] input, final DynamicOps<T> ops, final T prefix) {
                    return DataResult.error(() -> "Not Implemented");
                }

                @SuppressWarnings("unchecked")
                @Override
                public <T> DataResult<Pair<short[], T>> decode(final DynamicOps<T> ops, final T input) {
                    final DataResult<Stream<T>> listResult = ops.getStream(input);
                    if (listResult.isError()) {
                        return listResult.map(unused -> Pair.of(new short[0], input));
                    }

                    final T[] rangesData = (T[]) listResult.result().orElseThrow().toArray();
                    final ShortList ranges = new ShortArrayList(rangesData.length * 2 + 3);
                    ranges.add((short) 0x0020);
                    ranges.add((short) 0x00FF);

                    for (int i = 0; i < rangesData.length; i++) {
                        final T element = rangesData[i];

                        final DataResult<String> stringResult = ops.getStringValue(element);
                        if (stringResult.result().isPresent()) {
                            final String builtInRange = stringResult.result().get().toLowerCase(Locale.ROOT);
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
                                default -> {
                                    return DataResult.error(() -> "Unknown built-in range: " + builtInRange);
                                }
                            }
                            continue;
                        }

                        final DataResult<MapLike<T>> mapResult = ops.getMap(element);
                        if (mapResult.result().isPresent()) {
                            final MapLike<T> map = mapResult.result().get();

                            final DataResult<Number> min = ops.getNumberValue(map.get(ops.createString("min")));
                            if (min.error().isPresent()) {
                                return min.map(unused -> Pair.of(new short[0], input));
                            }
                            final DataResult<Number> max = ops.getNumberValue(map.get(ops.createString("max")));
                            if (max.error().isPresent()) {
                                return max.map(unused -> Pair.of(new short[0], input));
                            }

                            ranges.add(min.getOrThrow().shortValue());
                            ranges.add(max.getOrThrow().shortValue());
                            continue;
                        }

                        final int index = i;
                        return DataResult.error(() -> "Expected ranges[" + index + "] to be a string or map");
                    }
                    ranges.add((short) 0);
                    return DataResult.success(Pair.of(ranges.toShortArray(), input));
                }
            }.optionalFieldOf("ranges", ImGuiFontManager.DEFAULT_FONT_RANGES).forGetter(ImGuiFontRangesMetadataSection::ranges)
    ).apply(instance, ImGuiFontRangesMetadataSection::new));
    public static final MetadataSectionType<ImGuiFontRangesMetadataSection> TYPE = new MetadataSectionType<>(ImGuiMC.MOD_ID + ":font_ranges", CODEC);
}
*///?}