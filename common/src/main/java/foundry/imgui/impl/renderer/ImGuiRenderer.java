package foundry.imgui.impl.renderer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import foundry.imgui.api.ImGuiSampler;
import foundry.imgui.api.ImGuiTextureProvider;
import imgui.ImDrawData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NativeResource;

@ApiStatus.Internal
public interface ImGuiRenderer extends NativeResource {

    void init();

    void newFrame();

    void renderDrawData(ImDrawData drawData, RenderTarget renderTarget);

    void recreateFontsTexture();

    long getImGuiId(ImGuiTextureProvider texture, @Nullable ImGuiSampler sampler);
}
