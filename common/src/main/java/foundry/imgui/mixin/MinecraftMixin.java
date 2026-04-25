package foundry.imgui.mixin;

import foundry.imgui.impl.ImGuiMCImpl;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "runTick", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", args = "ldc=mouse"))
    public void beginFrame(final CallbackInfo ci) {
        if (ImGuiMCImpl.handler != null) {
            ImGuiMCImpl.handler.beginFrame();
        }
    }

    //? if >=26.1 {
    /*@Inject(method = "renderFrame", at=@At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V", args = "ldc=present"))
     *///? } elif >=1.21.6 {
    /*@Inject(method = "runTick", at=@At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", args = "ldc=blit"))
     *///? } else
    @Inject(method = "runTick", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", args = "ldc=updateDisplay"))
    public void endFrame(final CallbackInfo ci) {
        if (ImGuiMCImpl.handler != null) {
            ImGuiMCImpl.handler.endFrame();
        }
    }
}
