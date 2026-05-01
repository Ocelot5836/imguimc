package foundry.imgui.api.gizmo;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Optional singleton registry for named ImguiImguiGizmoScreen instances.
 * Useful when multiple editor panels need to share the same viewport.
 */
public final class ImguiGizmoRegistry {

    private static ImguiGizmoRegistry INSTANCE;

    private final Map<String, ImguiGizmoScreen> screens = new LinkedHashMap<>();

    private ImguiGizmoRegistry() {}

    public static ImguiGizmoRegistry getInstance() {
        if (INSTANCE == null) INSTANCE = new ImguiGizmoRegistry();
        return INSTANCE;
    }

    /** Registers a screen. Replaces any existing screen with the same id. */
    public ImguiGizmoRegistry register(String name, ImguiGizmoScreen screen) {
        screens.put(name, screen);
        return this;
    }

    /** Returns the screen with the given name, creating it with defaults if it doesn't exist. */
    public ImguiGizmoScreen getOrCreate(String name) {
        return screens.computeIfAbsent(name, k ->
                new ImguiGizmoScreen(k)
                        .setConfig(ImguiGizmoConfig.universal().build())
                        .setTheme(ImguiGizmoTheme.dark().build())
        );
    }

    public ImguiGizmoScreen get(String name) {
        return screens.get(name);
    }

    public void remove(String name) {
        screens.remove(name);
    }

    public Collection<ImguiGizmoScreen> all() {
        return Collections.unmodifiableCollection(screens.values());
    }

    public void clear() {
        screens.clear();
    }
}