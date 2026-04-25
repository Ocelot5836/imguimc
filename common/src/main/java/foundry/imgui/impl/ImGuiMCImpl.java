package foundry.imgui.impl;

import foundry.imgui.api.ImGuiMC;
import foundry.imgui.api.ImGuiMCEvents;
import imgui.ImGui;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApiStatus.Internal
public final class ImGuiMCImpl {

    public static final Logger LOGGER = LoggerFactory.getLogger(ImGuiMC.MOD_ID);

    public static ImGuiHandler handler;

    public static void init(){
        ImGuiMCEvents.INSTANCE.preRenderImGuiEvents(()->{
            if (ImGui.begin("Test")) {

            }
            ImGui.end();
        });
    }

    public static void initHandler() {
        //? if <=1.21.8 {
        /*final long window = Minecraft.getInstance().getWindow().getWindow();
        *///? } else {
        final long window = Minecraft.getInstance().getWindow().handle();
        //? }

        try {
            handler = new ImGuiHandler(window);
        } catch (final Throwable t) {
            LOGGER.error("Failed to load ImGui, disabling", t);
            handler = null;
        }
    }
}
