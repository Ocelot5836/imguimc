package foundry.imgui.fabric.api.event;

import foundry.imgui.api.ImGuiMC;
import foundry.imgui.api.event.RegisterImGuiFontsEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Fired when the font atlas is rebuilt before the next ImGui frame.
 *
 * @see ImGuiMC#rebuildFonts()
 * @since 1.0.0
 */
public interface RegisterImGuiFontsEventFabric extends RegisterImGuiFontsEvent {

    Event<RegisterImGuiFontsEvent> EVENT = EventFactory.createArrayBacked(RegisterImGuiFontsEvent.class, (atlas, defaultFont, fontScale) -> {
    }, events -> (atlas, defaultFont, fontScale) -> {
        for (RegisterImGuiFontsEvent event : events) {
            event.registerImGuiFonts(atlas, defaultFont, fontScale);
        }
    });
}
