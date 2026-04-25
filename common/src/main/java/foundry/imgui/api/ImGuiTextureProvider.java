package foundry.imgui.api;

import org.jetbrains.annotations.ApiStatus;

/**
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface ImGuiTextureProvider {

    @ApiStatus.Internal
    default long imguimc$id() {
        throw new AssertionError();
    }

    @ApiStatus.Internal
    default void imguimc$setId(final long id) {
        throw new AssertionError();
    }
}
