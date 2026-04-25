package foundry.imgui.api;

import com.mojang.blaze3d.systems.RenderSystem;
import foundry.imgui.api.event.RegisterImGuiFontsEvent;
import foundry.imgui.impl.ActiveContextImpl;
import foundry.imgui.impl.ImGuiMCImpl;
import foundry.imgui.impl.font.ImGuiFontManager;
import imgui.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

/**
 * @since 1.0.0
 */
public interface ImGuiMC {

    ResourceLocation FONT_JETBRAINS_MONO = ResourceLocation.fromNamespaceAndPath(ImGuiMC.MOD_ID, "jetbrains_mono");
    ResourceLocation FONT_REMIXICON = ResourceLocation.fromNamespaceAndPath(ImGuiMC.MOD_ID, "remixicon");
    ResourceLocation FONT_DEFAULT = FONT_JETBRAINS_MONO;

    String MOD_ID = "imguimc";

    /**
     * Sets up the ImGui context for running code.
     *
     * @return The current context or <code>null</code> if there is no active context
     */
    static @Nullable ActiveContext withImGui() {
        if (!RenderSystem.isOnRenderThread()) {
            ImGuiMCImpl.LOGGER.error("Called Veil#withImGui() on another thread");
            return null;
        }

        if (ImGuiMCImpl.handler != null) {
            ImGuiMCImpl.handler.start();
            return ActiveContextImpl.INSTANCE;
        }

        return null;
    }

    /**
     * Schedules the font atlas to be rebuilt at the start of the next frame.
     * <br>
     * {@link RegisterImGuiFontsEvent} will be fired just before the texture is created.
     */
    static void rebuildFonts() {
        if (ImGuiMCImpl.handler != null) {
            ImGuiMCImpl.handler.updateFonts();
        }
    }

    /**
     * Fetches a data-driven font by name.
     *
     * @param name   The name of the font
     * @param bold   Whether to request a bold version
     * @param italic Whether to request an italic version
     * @return The font to use
     * @see ImGuiFontManager
     */
    static ImFont getFont(@Nullable final ResourceLocation name, final boolean bold, final boolean italic) {
        if (ImGuiMCImpl.handler == null) {
            throw new IllegalStateException("ImGui is not loaded");
        }
        return ImGuiMCImpl.handler.getFontManager().getFont(name, bold, italic);
    }

    /**
     * Fetches the default font.
     *
     * @param bold   Whether to request a bold version
     * @param italic Whether to request an italic version
     * @return The font to use
     * @see ImGuiFontManager
     */
    static ImFont getFont(final boolean bold, final boolean italic) {
        return getFont(null, bold, italic);
    }

    /**
     * @return <code>true</code> if ImGui is currently loaded and able to be used
     */
    static boolean isImguiLoaded() {
        return ImGuiMCImpl.handler != null;
    }

    static void image(final ImGuiTextureProvider userTexture, final ImVec2 size) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), size);
    }

    static void image(final ImGuiTextureProvider userTexture, final float sizeX, final float sizeY) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), sizeX, sizeY);
    }

    static void image(final ImGuiTextureProvider userTexture, final ImVec2 size, final ImVec2 uv0) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), size, uv0);
    }

    static void image(final ImGuiTextureProvider userTexture, final float sizeX, final float sizeY, final float uv0X, final float uv0Y) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), sizeX, sizeY, uv0X, uv0Y);
    }

    static void image(final ImGuiTextureProvider userTexture, final ImVec2 size, final ImVec2 uv0, final ImVec2 uv1) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), size, uv0, uv1);
    }

    static void image(final ImGuiTextureProvider userTexture, final float sizeX, final float sizeY, final float uv0X, final float uv0Y, final float uv1X, final float uv1Y) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), sizeX, sizeY, uv0X, uv0Y, uv1X, uv1Y);
    }

    static void image(final ImGuiTextureProvider userTexture, final ImVec2 size, final ImVec2 uv0, final ImVec2 uv1, final ImVec4 tintCol) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), size, uv0, uv1, tintCol);
    }

    static void image(final ImGuiTextureProvider userTexture, final float sizeX, final float sizeY, final float uv0X, final float uv0Y, final float uv1X, final float uv1Y, final float tintColX, final float tintColY, final float tintColZ, final float tintColW) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), sizeX, sizeY, uv0X, uv0Y, uv1X, uv1Y, tintColX, tintColY, tintColZ, tintColW);
    }

    static void image(final ImGuiTextureProvider userTexture, final ImVec2 size, final ImVec2 uv0, final ImVec2 uv1, final ImVec4 tintCol, final ImVec4 borderCol) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), size, uv0, uv1, tintCol, borderCol);
    }

    static void image(final ImGuiTextureProvider userTexture, final float sizeX, final float sizeY, final float uv0X, final float uv0Y, final float uv1X, final float uv1Y, final float tintColX, final float tintColY, final float tintColZ, final float tintColW, final float borderColX, final float borderColY, final float borderColZ, final float borderColW) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), sizeX, sizeY, uv0X, uv0Y, uv1X, uv1Y, tintColX, tintColY, tintColZ, tintColW, borderColX, borderColY, borderColZ, borderColW);
    }

    static void image(final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final ImVec2 size) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), size);
    }

    static void image(final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final float sizeX, final float sizeY) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), sizeX, sizeY);
    }

    static void image(final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final ImVec2 size, final ImVec2 uv0) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), size, uv0);
    }

    static void image(final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final float sizeX, final float sizeY, final float uv0X, final float uv0Y) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), sizeX, sizeY, uv0X, uv0Y);
    }

    static void image(final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final ImVec2 size, final ImVec2 uv0, final ImVec2 uv1) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), size, uv0, uv1);
    }

    static void image(final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final float sizeX, final float sizeY, final float uv0X, final float uv0Y, final float uv1X, final float uv1Y) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), sizeX, sizeY, uv0X, uv0Y, uv1X, uv1Y);
    }

    static void image(final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final ImVec2 size, final ImVec2 uv0, final ImVec2 uv1, final ImVec4 tintCol) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), size, uv0, uv1, tintCol);
    }

    static void image(final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final float sizeX, final float sizeY, final float uv0X, final float uv0Y, final float uv1X, final float uv1Y, final float tintColX, final float tintColY, final float tintColZ, final float tintColW) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), sizeX, sizeY, uv0X, uv0Y, uv1X, uv1Y, tintColX, tintColY, tintColZ, tintColW);
    }

    static void image(final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final ImVec2 size, final ImVec2 uv0, final ImVec2 uv1, final ImVec4 tintCol, final ImVec4 borderCol) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), size, uv0, uv1, tintCol, borderCol);
    }

    static void image(final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final float sizeX, final float sizeY, final float uv0X, final float uv0Y, final float uv1X, final float uv1Y, final float tintColX, final float tintColY, final float tintColZ, final float tintColW, final float borderColX, final float borderColY, final float borderColZ, final float borderColW) {
        ImGui.image(ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), sizeX, sizeY, uv0X, uv0Y, uv1X, uv1Y, tintColX, tintColY, tintColZ, tintColW, borderColX, borderColY, borderColZ, borderColW);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, final ImVec2 size) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), size);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, final float sizeX, final float sizeY) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), sizeX, sizeY);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, final ImVec2 size, final ImVec2 uv0) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), size, uv0);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, final float sizeX, final float sizeY, final float uv0X, final float uv0Y) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), sizeX, sizeY, uv0X, uv0Y);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, final ImVec2 size, final ImVec2 uv0, final ImVec2 uv1) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), size, uv0, uv1);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, final float sizeX, final float sizeY, final float uv0X, final float uv0Y, final float uv1X, final float uv1Y) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), sizeX, sizeY, uv0X, uv0Y, uv1X, uv1Y);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, final ImVec2 size, final ImVec2 uv0, final ImVec2 uv1, final ImVec4 bgCol) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), size, uv0, uv1, bgCol);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, final float sizeX, final float sizeY, final float uv0X, final float uv0Y, final float uv1X, final float uv1Y, final float bgColX, final float bgColY, final float bgColZ, final float bgColW) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), sizeX, sizeY, uv0X, uv0Y, uv1X, uv1Y, bgColX, bgColY, bgColZ, bgColW);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, final ImVec2 size, final ImVec2 uv0, final ImVec2 uv1, final ImVec4 bgCol, final ImVec4 tintCol) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), size, uv0, uv1, bgCol, tintCol);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, final float sizeX, final float sizeY, final float uv0X, final float uv0Y, final float uv1X, final float uv1Y, final float bgColX, final float bgColY, final float bgColZ, final float bgColW, final float tintColX, final float tintColY, final float tintColZ, final float tintColW) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, null), sizeX, sizeY, uv0X, uv0Y, uv1X, uv1Y, bgColX, bgColY, bgColZ, bgColW, tintColX, tintColY, tintColZ, tintColW);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final ImVec2 size) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), size);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final float sizeX, final float sizeY) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), sizeX, sizeY);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final ImVec2 size, final ImVec2 uv0) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), size, uv0);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final float sizeX, final float sizeY, final float uv0X, final float uv0Y) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), sizeX, sizeY, uv0X, uv0Y);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final ImVec2 size, final ImVec2 uv0, final ImVec2 uv1) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), size, uv0, uv1);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final float sizeX, final float sizeY, final float uv0X, final float uv0Y, final float uv1X, final float uv1Y) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), sizeX, sizeY, uv0X, uv0Y, uv1X, uv1Y);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final ImVec2 size, final ImVec2 uv0, final ImVec2 uv1, final ImVec4 bgCol) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), size, uv0, uv1, bgCol);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final float sizeX, final float sizeY, final float uv0X, final float uv0Y, final float uv1X, final float uv1Y, final float bgColX, final float bgColY, final float bgColZ, final float bgColW) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), sizeX, sizeY, uv0X, uv0Y, uv1X, uv1Y, bgColX, bgColY, bgColZ, bgColW);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final ImVec2 size, final ImVec2 uv0, final ImVec2 uv1, final ImVec4 bgCol, final ImVec4 tintCol) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), size, uv0, uv1, bgCol, tintCol);
    }

    static boolean imageButton(final String strId, final ImGuiTextureProvider userTexture, @Nullable final ImGuiSampler sampler, final float sizeX, final float sizeY, final float uv0X, final float uv0Y, final float uv1X, final float uv1Y, final float bgColX, final float bgColY, final float bgColZ, final float bgColW, final float tintColX, final float tintColY, final float tintColZ, final float tintColW) {
        return ImGui.imageButton(strId, ImGuiMCImpl.handler.getRenderer().getImGuiId(userTexture, sampler), sizeX, sizeY, uv0X, uv0Y, uv1X, uv1Y, bgColX, bgColY, bgColZ, bgColW, tintColX, tintColY, tintColZ, tintColW);
    }

    interface ActiveContext extends AutoCloseable {

        ImGuiIO io();

        @Override
        void close();
    }
}
