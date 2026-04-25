package foundry.imgui.api;

import foundry.imgui.api.event.ImGuiLoadEvent;
import foundry.imgui.api.event.RegisterImGuiFontsEvent;
import foundry.imgui.api.event.RenderImGuiEvents;

import java.util.ServiceLoader;

/**
 * Manages platform-specific implementations of event subscriptions.
 *
 * @author Ocelot
 * @since 1.0.0
 */
public interface ImGuiMCEvents {

    ImGuiMCEvents INSTANCE = ServiceLoader.load(ImGuiMCEvents.class).findFirst().orElseThrow(() -> new RuntimeException("Failed to find platform event provider"));

    /**
     * @since 1.1.0
     */
    void onImGuiLoad(ImGuiLoadEvent event);

    void onRegisterImGuiFonts(RegisterImGuiFontsEvent event);

    void preRenderImGuiEvents(RenderImGuiEvents.Pre event);

    void postRenderImGuiEvents(RenderImGuiEvents.Post event);

}
