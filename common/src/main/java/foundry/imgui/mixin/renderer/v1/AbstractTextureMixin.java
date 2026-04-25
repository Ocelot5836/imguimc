package foundry.imgui.mixin.renderer.v1;

//? if >=1.21.6 {

/*import com.mojang.blaze3d.textures.GpuTextureView;
import foundry.imgui.api.ImGuiTextureProvider;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractTexture.class)
public abstract class AbstractTextureMixin implements ImGuiTextureProvider {

    @Shadow
    public abstract GpuTextureView getTextureView();

    @Override
    public long imguimc$id() {
        return this.getTextureView().imguimc$id();
    }

    @Override
    public void imguimc$setId(final long id) {
        this.getTextureView().imguimc$setId(id);
    }
}

*///?}