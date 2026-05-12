package foundry.imgui.impl;

import com.mojang.blaze3d.pipeline.RenderTarget;
import foundry.imgui.api.ImGuiMC;
import foundry.imgui.impl.platform.ImGuiMCPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApiStatus.Internal
public final class ImGuiMCImpl {

    public static final Logger LOGGER = LoggerFactory.getLogger(ImGuiMC.MOD_ID);

    public static ImGuiHandler handler;

    public static void init() {
    }

    public static void initHandler() {
        //? if >=1.21.9 {
        /*final long window = Minecraft.getInstance().getWindow().handle();
        *///? } else {
        final long window = Minecraft.getInstance().getWindow().getWindow();
         //? }

        try {
            handler = new ImGuiHandler(window);
            ImGuiMCPlatform.INSTANCE.afterImGuiLoad();
        } catch (final Throwable t) {
            LOGGER.error("Failed to load ImGui, disabling", t);
            handler = null;
        }
    }

    public static ResourceLocation path(final String path) {
        return ResourceLocation.fromNamespaceAndPath(ImGuiMC.MOD_ID, path);
    }

    public static RenderTarget getMainRenderTarget() {
        //? if >= 26.2-snapshot-3 {
        /*return Minecraft.getInstance().gameRenderer.mainRenderTarget();
        *///? } else {
        return Minecraft.getInstance().getMainRenderTarget();
         //? }
    }
}
