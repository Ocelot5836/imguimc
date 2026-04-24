package foundry.imgui.impl;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import imgui.ImGuiIO;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public enum ActiveContextImpl implements ImGuiMC.ActiveContext {
    INSTANCE;

    @Override
    public ImGuiIO io() {
        return ImGui.getIO();
    }

    @Override
    public void close() {
        ImGuiMCImpl.handler.stop();
    }
}
