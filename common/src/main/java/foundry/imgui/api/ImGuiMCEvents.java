package foundry.imgui.api;

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

    void onRegisterImGuiFonts(RegisterImGuiFontsEvent event);

    void preRenderImGuiEvents(RenderImGuiEvents.Pre event);

    void postRenderImGuiEvents(RenderImGuiEvents.Post event);

}
