package foundry.imgui.neoforge.api.event;

import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.ApiStatus;

/**
 * Events fired when the ImGui frame begins and ends.
 *
 * @since 1.0.0
 */
public abstract class RenderImGuiEventsNeoforge extends Event {

    public static final class Pre extends RenderImGuiEventsNeoforge {

        @ApiStatus.Internal
        public Pre() {
        }
    }

    public static final class Post extends RenderImGuiEventsNeoforge {

        @ApiStatus.Internal
        public Post() {
        }
    }
}
