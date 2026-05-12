package foundry.imgui.fabric.impl;

import foundry.imgui.api.ImGuiMCEvents;
import foundry.imgui.api.event.ImGuiLoadEvent;
import foundry.imgui.api.event.RegisterImGuiFontsEvent;
import foundry.imgui.api.event.RenderImGuiEvents;
import foundry.imgui.fabric.api.event.RegisterImGuiFontsEventFabric;
import foundry.imgui.fabric.api.event.RenderImGuiEventsFabric;
import foundry.imgui.fabric.api.event.ImGuiLoadEventFabric;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ImGuiMCEventsImpl implements ImGuiMCEvents {

    @Override
    public void onImGuiLoad(final ImGuiLoadEvent event) {
        ImGuiLoadEventFabric.EVENT.register(event);
    }

    @Override
    public void onRegisterImGuiFonts(final RegisterImGuiFontsEvent event) {
        RegisterImGuiFontsEventFabric.EVENT.register(event);
    }

    @Override
    public void preRenderImGuiEvent(final RenderImGuiEvents.Pre event) {
        RenderImGuiEventsFabric.PRE.register(event);
    }

    @Override
    public void postRenderImGuiEvent(final RenderImGuiEvents.Post event) {
        RenderImGuiEventsFabric.POST.register(event);
    }
}
