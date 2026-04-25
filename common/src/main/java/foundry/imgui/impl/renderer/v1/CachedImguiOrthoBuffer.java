package foundry.imgui.impl.renderer.v1;

//? if >=1.21.11 {

/*import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.systems.RenderSystem;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

@ApiStatus.Internal
public class CachedImguiOrthoBuffer implements AutoCloseable {

    private final GpuBuffer buffer;
    private final GpuBufferSlice slice;
    private final float zNear;
    private final float zFar;
    private final Matrix4f projectionMatrix;

    private float left;
    private float right;
    private float bottom;
    private float top;

    public CachedImguiOrthoBuffer(final float zNear, final float zFar) {
        this.zNear = zNear;
        this.zFar = zFar;
        this.projectionMatrix = new Matrix4f();
        this.buffer = RenderSystem.getDevice().createBuffer(
                () -> "Projection matrix UBO ImGui",
                GpuBuffer.USAGE_COPY_DST | GpuBuffer.USAGE_UNIFORM,
                RenderSystem.PROJECTION_MATRIX_UBO_SIZE);
        this.slice = this.buffer.slice(0, RenderSystem.PROJECTION_MATRIX_UBO_SIZE);
    }

    public GpuBufferSlice getBuffer(final float left, final float right, final float bottom, final float top) {
        if (this.left != left || this.right != right || this.bottom != bottom || this.top != top) {
            final Matrix4f matrix4f = this.projectionMatrix.setOrtho(left, right, bottom, top, this.zNear, this.zFar);

            try (final MemoryStack stack = MemoryStack.stackPush()) {
                final ByteBuffer buffer = Std140Builder.onStack(stack, RenderSystem.PROJECTION_MATRIX_UBO_SIZE).putMat4f(matrix4f).get();
                RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.buffer.slice(), buffer);
            }

            this.left = left;
            this.right = right;
            this.bottom = bottom;
            this.top = top;
        }

        return this.slice;
    }

    @Override
    public void close() {
        this.buffer.close();
    }
}
*///?}