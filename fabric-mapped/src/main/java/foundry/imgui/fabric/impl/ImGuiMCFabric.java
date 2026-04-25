package foundry.imgui.fabric.impl;

import foundry.imgui.impl.ImGuiMCImpl;
import net.fabricmc.api.ClientModInitializer;

public class ImGuiMCFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ImGuiMCImpl.init();
    }
}
