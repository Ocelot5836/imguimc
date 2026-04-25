package foundry.imgui.neoforge.impl;

import foundry.imgui.api.ImGuiMCEvents;
import foundry.imgui.api.event.ImGuiLoadEvent;
import foundry.imgui.api.event.RegisterImGuiFontsEvent;
import foundry.imgui.api.event.RenderImGuiEvents;
import foundry.imgui.neoforge.api.event.ImGuiLoadEventNeoforge;
import foundry.imgui.neoforge.api.event.RegisterImGuiFontsEventNeoforge;
import foundry.imgui.neoforge.api.event.RenderImGuiEventsNeoforge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

@ApiStatus.Internal
public class ImGuiMCEventsImpl implements ImGuiMCEvents {

    @Override
    public void onImGuiLoad(final ImGuiLoadEvent event) {
        final IEventBus modBus = ModLoadingContext.get().getActiveContainer().getEventBus();
        Objects.requireNonNull(modBus, "Call this in your mod constructor");
        modBus.<ImGuiLoadEventNeoforge>addListener(forgeEvent -> event.afterImGuiLoad());
    }

    @Override
    public void onRegisterImGuiFonts(final RegisterImGuiFontsEvent event) {
        NeoForge.EVENT_BUS.<RegisterImGuiFontsEventNeoforge>addListener(forgeEvent -> event.registerImGuiFonts(forgeEvent.getAtlas(), forgeEvent.getDefaultFont(), forgeEvent.getFontScale()));
    }

    @Override
    public void preRenderImGuiEvents(final RenderImGuiEvents.Pre event) {
        NeoForge.EVENT_BUS.<RenderImGuiEventsNeoforge.Pre>addListener(forgeEvent -> event.drawImGuiPre());
    }

    @Override
    public void postRenderImGuiEvents(final RenderImGuiEvents.Post event) {
        NeoForge.EVENT_BUS.<RenderImGuiEventsNeoforge.Post>addListener(forgeEvent -> event.drawImGuiPost());
    }
}
