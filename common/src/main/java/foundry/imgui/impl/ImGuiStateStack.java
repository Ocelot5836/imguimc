package foundry.imgui.impl;

import imgui.ImGui;
import imgui.extension.implot.ImPlot;
import imgui.extension.implot.ImPlotContext;
import imgui.internal.ImGuiContext;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayDeque;
import java.util.Deque;

@ApiStatus.Internal
public class ImGuiStateStack {

    private static final Deque<State> STATE_STACK = new ArrayDeque<>(1);

    public static void push() {
        final long imGuiContext = ImGui.getCurrentContext().ptr;
        final long imPlotContext = ImPlot.getCurrentContext().ptr;
        STATE_STACK.add(new State(imGuiContext, imPlotContext));
    }

    public static void pop() {
        if (STATE_STACK.isEmpty()) {
            return;
        }

        final State state = STATE_STACK.removeLast();
        ImGui.setCurrentContext(new ImGuiContext(state.imGuiContext));
        ImPlot.setCurrentContext(new ImPlotContext(state.imPlotContext));
    }

    public static void forcePop() {
        if (STATE_STACK.size() > 1) {
            ImGuiMCImpl.LOGGER.error("Mismatched begin/end during frame");
        }
        while (STATE_STACK.size() > 1) {
            STATE_STACK.removeLast();
        }
        pop();
    }

    private record State(long imGuiContext, long imPlotContext) {
    }
}
