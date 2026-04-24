package foundry.imgui.fabric.api.event;

import foundry.imgui.api.event.RenderImGuiEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Events fired when the ImGui frame begins and ends.
 *
 * @since 1.0.0
 */
public final class RenderImGuiEventsFabric {

    public static final Event<RenderImGuiEvents.Pre> PRE = EventFactory.createArrayBacked(RenderImGuiEvents.Pre.class, () -> {
    }, events -> () -> {
        for (RenderImGuiEvents.Pre event : events) {
            event.drawImGuiPre();
        }
    });
    public static final Event<RenderImGuiEvents.Post> POST = EventFactory.createArrayBacked(RenderImGuiEvents.Post.class, () -> {
    }, events -> () -> {
        for (RenderImGuiEvents.Post event : events) {
            event.drawImGuiPost();
        }
    });
}
