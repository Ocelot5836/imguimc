package foundry.imgui.neoforge.api.event;

import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired after ImGui loads successfully.
 *
 * @since 1.1.0
 */
public final class ImGuiLoadEventNeoforge extends Event implements IModBusEvent {

    @ApiStatus.Internal
    public ImGuiLoadEventNeoforge() {
    }
}
