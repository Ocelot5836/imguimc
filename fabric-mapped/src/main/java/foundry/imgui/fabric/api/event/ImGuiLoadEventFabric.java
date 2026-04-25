package foundry.imgui.fabric.api.event;

import foundry.imgui.api.event.ImGuiLoadEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Fired after ImGui loads successfully.
 *
 * @since 1.1.0
 */
public interface ImGuiLoadEventFabric extends ImGuiLoadEvent {

    Event<ImGuiLoadEvent> EVENT = EventFactory.createArrayBacked(ImGuiLoadEvent.class, () -> {
    }, events -> () -> {
        for (ImGuiLoadEvent event : events) {
            event.afterImGuiLoad();
        }
    });
}
