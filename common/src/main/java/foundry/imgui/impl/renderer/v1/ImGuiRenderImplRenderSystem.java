package foundry.imgui.impl.renderer.v1;

//? if >=1.21.6 {

/*import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.CompiledRenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.shaders.ShaderType;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.*;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import foundry.imgui.api.ImGuiMC;
import foundry.imgui.api.ImGuiTextureProvider;
import foundry.imgui.impl.ImGuiMCImpl;
import foundry.imgui.impl.renderer.ImGuiRenderer;
import imgui.*;
import imgui.callback.ImPlatformFuncViewport;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiViewportFlags;
import imgui.type.ImInt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.system.MemoryUtil;

import java.util.*;

@ApiStatus.Internal
public class ImGuiRenderImplRenderSystem implements ImGuiRenderer {

    private static final VertexFormat VERTEX_FORMAT;

    static {
        VertexFormatElement posElement = null;

        for (int i = 7; i < VertexFormatElement.MAX_COUNT; i++) {
            final VertexFormatElement element = VertexFormatElement.byId(i);
            if (element == null) {
                posElement = VertexFormatElement.register(i, 0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.POSITION, 2);
                break;
            }
        }

        if (posElement == null) {
            throw new IllegalStateException("Failed to create vertex format");
        }

        VERTEX_FORMAT = VertexFormat.builder()
                .add("Position", posElement)
                .add("UV0", VertexFormatElement.UV0)
                .add("Color", VertexFormatElement.COLOR)
                .build();
    }


    private static final String VERTEX_SHADER = """
            #version 410 core
            layout (location = 0) in vec2 Position;
            layout (location = 1) in vec2 UV;
            layout (location = 2) in vec4 Color;
            layout(std140) uniform Projection {
                mat4 ProjMtx;
            };
            out vec2 Frag_UV;
            out vec4 Frag_Color;
            void main()
            {
                Frag_UV = UV;
                Frag_Color = Color;
                gl_Position = ProjMtx * vec4(Position.xy,0,1);
            }
            """;
    private static final String FRAGMENT_SHADER = """
            #version 410 core
            in vec2 Frag_UV;
            in vec4 Frag_Color;
            uniform sampler2D Texture;
            layout (location = 0) out vec4 Out_Color;
            void main()
            {
                Out_Color = Frag_Color * texture(Texture, Frag_UV.st);
            }
            """;
    private static final Map<ResourceLocation, String> SOURCES_MAP = Map.of(
            ResourceLocation.fromNamespaceAndPath(ImGuiMC.MOD_ID, "shader_vertex"), VERTEX_SHADER,
            ResourceLocation.fromNamespaceAndPath(ImGuiMC.MOD_ID, "shader_fragment"), FRAGMENT_SHADER
    );

    private static final RenderPipeline PIPELINE = RenderPipeline.builder()
            .withLocation(ResourceLocation.fromNamespaceAndPath(ImGuiMC.MOD_ID, "pipeline/imgui"))
            .withVertexShader(ResourceLocation.fromNamespaceAndPath(ImGuiMC.MOD_ID, "shader_vertex"))
            .withFragmentShader(ResourceLocation.fromNamespaceAndPath(ImGuiMC.MOD_ID, "shader_fragment"))
            .withSampler("Texture")
            .withUniform("Projection", UniformType.UNIFORM_BUFFER)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .withDepthWrite(false)
            .withCull(false)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withVertexFormat(VERTEX_FORMAT, VertexFormat.Mode.TRIANGLES)
            .build();

    private static String getShaderSource(final ResourceLocation name, final ShaderType shaderType) {
        return SOURCES_MAP.get(name);
    }

    public long addTexture(final GpuTextureView view) {
        this.data.textures.add(view);
        return this.data.textures.size() + 1;
    }

    /^*
     * Data class to store implementation specific fields.
     * Same as {@code ImGui_ImplOpenGL3_Data}.
     ^/
    protected static class Data {
        protected GpuTextureView fontTextureView;
        protected GpuTexture fontTexture;
        protected CompiledRenderPipeline pipelineHandle;
        protected CachedImguiOrthoBuffer projectionMatrixBuffer;
        protected List<MappableRingBuffer> vertexData = new ArrayList<>();
        protected List<MappableRingBuffer> indexData = new ArrayList<>();
        protected int elementSize;
        protected List<GpuTextureView> textures = new ArrayList<>();
    }

    /^*
     * Internal class to store containers for frequently used arrays.
     * This class helps minimize the number of object allocations on the JVM side,
     * thereby improving performance and reducing garbage collection overhead.
     ^/
    private static final class Properties {
        private final ImVec4 clipRect = new ImVec4();
    }

    protected Data data = null;
    private final Properties props = new Properties();

    protected Data newData() {
        return new Data();
    }

    private void clearTextures() {
        if (this.data.textures == null || this.data.textures.isEmpty()) {
            return;
        }

        final Iterator<GpuTextureView> iterator = this.data.textures.iterator();
        while (iterator.hasNext()) {
            ((ImGuiTextureProvider) iterator.next()).imguimc$setId(0);
            iterator.remove();
        }
    }

    private void clearVertexData(final int maxCommands) {
        final int removeVertices = this.data.vertexData.size() - maxCommands;
        if (removeVertices > 0) {
            final Iterator<MappableRingBuffer> iterator = this.data.vertexData.iterator();
            for (int i = 0; i < removeVertices; i++) {
                iterator.next().close();
                iterator.remove();
            }
        }

        final int removeIndices = this.data.indexData.size() - maxCommands;
        if (removeIndices > 0) {
            final Iterator<MappableRingBuffer> iterator = this.data.indexData.iterator();
            for (int i = 0; i < removeIndices; i++) {
                iterator.next().close();
                iterator.remove();
            }
        }
    }

    @Override
    public void init() {
        this.data = this.newData();

        final GpuDevice device = RenderSystem.getDevice();

        final ImGuiIO io = ImGui.getIO();
        io.setBackendRendererName("imgui-java_impl_" + device.getBackendName());

        // We can honor the ImDrawCmd::VtxOffset field, allowing for large meshes.
        io.addBackendFlags(ImGuiBackendFlags.RendererHasVtxOffset);

        // We can create multi-viewports on the Renderer side (optional)
        io.addBackendFlags(ImGuiBackendFlags.RendererHasViewports);

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            this.initPlatformInterface();
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private void renderDrawData(final ImDrawData drawData, final OptionalInt clearColor) {
        // Avoid rendering when minimized, scale coordinates for retina displays (screen coordinates != framebuffer coordinates)
        final int fbWidth = (int) (drawData.getDisplaySizeX() * drawData.getFramebufferScaleX());
        final int fbHeight = (int) (drawData.getDisplaySizeY() * drawData.getFramebufferScaleY());
        if (fbWidth <= 0 || fbHeight <= 0) {
            this.clearTextures();
            this.clearVertexData(0);
            return;
        }

        final int cmdListsCount = drawData.getCmdListsCount();
        if (cmdListsCount <= 0) {
            this.clearTextures();
            this.clearVertexData(0);
            return;
        }

        final float L = drawData.getDisplayPosX();
        final float R = drawData.getDisplayPosX() + drawData.getDisplaySizeX();
        final float T = drawData.getDisplayPosY();
        final float B = drawData.getDisplayPosY() + drawData.getDisplaySizeY();

        // Will project scissor/clipping rectangles into framebuffer space
        final float clipOffX = drawData.getDisplayPosX(); // (0,0) unless using multi-viewports
        final float clipOffY = drawData.getDisplayPosY(); // (0,0) unless using multi-viewports
        final float clipScaleX = drawData.getFramebufferScaleX(); // (1,1) unless using retina display which are often (2,2)
        final float clipScaleY = drawData.getFramebufferScaleY(); // (1,1) unless using retina display which are often (2,2)

        if (ImDrawData.sizeOfImDrawIdx() != this.data.elementSize) {
            final Iterator<MappableRingBuffer> iterator = this.data.indexData.iterator();
            while (iterator.hasNext()) {
                iterator.next().close();
                iterator.remove();
            }
        }

        this.data.elementSize = ImDrawData.sizeOfImDrawIdx();
        this.clearVertexData(cmdListsCount);

        if (this.data.projectionMatrixBuffer == null) {
            this.data.projectionMatrixBuffer = new CachedImguiOrthoBuffer(-1.0F, 1.0F);
        }

        final GpuDevice device = RenderSystem.getDevice();
        device.precompilePipeline(PIPELINE, ImGuiRenderImplRenderSystem::getShaderSource);

        final CommandEncoder commandEncoder = device.createCommandEncoder();

        // Set up buffers
        for (int n = 0; n < cmdListsCount; n++) {
            final MappableRingBuffer vertexBuffer;
            final int vertexBufferSize = drawData.getCmdListVtxBufferSize(n) * ImDrawData.sizeOfImDrawVert();

            if (n >= this.data.vertexData.size()) {
                final int index = n;
                vertexBuffer = new MappableRingBuffer(() -> "ImGui Vertex Buffer " + index, GpuBuffer.USAGE_MAP_WRITE | GpuBuffer.USAGE_VERTEX, vertexBufferSize);
                this.data.vertexData.add(vertexBuffer);
            } else {
                final MappableRingBuffer buffer = this.data.vertexData.get(n);

                if (buffer.size() >= vertexBufferSize) {
                    vertexBuffer = buffer;
                } else {
                    buffer.close();
                    final int index = n;
                    vertexBuffer = new MappableRingBuffer(() -> "ImGui Vertex Buffer " + index, GpuBuffer.USAGE_MAP_WRITE | GpuBuffer.USAGE_VERTEX, vertexBufferSize);
                    this.data.vertexData.set(n, vertexBuffer);
                }
            }

            final MappableRingBuffer indexBuffer;
            final int indexBufferSize = drawData.getCmdListIdxBufferSize(n) * this.data.elementSize;

            if (n >= this.data.indexData.size()) {
                final int index = n;
                indexBuffer = new MappableRingBuffer(() -> "ImGui Index Buffer " + index, GpuBuffer.USAGE_MAP_WRITE | GpuBuffer.USAGE_INDEX, indexBufferSize);
                this.data.indexData.add(indexBuffer);
            } else {
                final MappableRingBuffer buffer = this.data.indexData.get(n);

                if (buffer.size() >= indexBufferSize) {
                    indexBuffer = buffer;
                } else {
                    buffer.close();
                    final int index = n;
                    indexBuffer = new MappableRingBuffer(() -> "ImGui Index Buffer " + index, GpuBuffer.USAGE_MAP_WRITE | GpuBuffer.USAGE_INDEX, indexBufferSize);
                    this.data.indexData.set(n, indexBuffer);
                }
            }

            try (final GpuBuffer.MappedView vertexView = commandEncoder
                    .mapBuffer(vertexBuffer.currentBuffer().slice(0, vertexBufferSize), false, true);
                 final GpuBuffer.MappedView indexView = commandEncoder
                         .mapBuffer(indexBuffer.currentBuffer().slice(0, indexBufferSize), false, true)) {
                MemoryUtil.memCopy(drawData.getCmdListVtxBufferData(n), vertexView.data());
                MemoryUtil.memCopy(drawData.getCmdListIdxBufferData(n), indexView.data());
            }
        }

        // TODO viewport

        final GpuBufferSlice projectionMatrixBuffer = this.data.projectionMatrixBuffer.getBuffer(L, R, B, T);
        final RenderTarget renderTarget = Minecraft.getInstance().getMainRenderTarget();
        try (final RenderPass renderPass = commandEncoder.createRenderPass(() -> "ImGui", renderTarget.getColorTextureView(), clearColor)) {
            renderPass.setPipeline(PIPELINE);
            renderPass.setUniform("Projection", projectionMatrixBuffer);

            // Render command lists
            for (int n = 0; n < cmdListsCount; n++) {
                final GpuBuffer vertexBuffer = this.data.vertexData.get(n).currentBuffer();
                final GpuBuffer indexBuffer = this.data.indexData.get(n).currentBuffer();

                renderPass.setVertexBuffer(0, vertexBuffer);
                renderPass.setIndexBuffer(indexBuffer, this.data.elementSize == 2 ? VertexFormat.IndexType.SHORT : VertexFormat.IndexType.INT);

                final int cmdBufferSize = drawData.getCmdListCmdBufferSize(n);
                for (int cmdIdx = 0; cmdIdx < cmdBufferSize; cmdIdx++) {
                    drawData.getCmdListCmdBufferClipRect(this.props.clipRect, n, cmdIdx);

                    final float clipMinX = (this.props.clipRect.x - clipOffX) * clipScaleX;
                    final float clipMinY = (this.props.clipRect.y - clipOffY) * clipScaleY;
                    final float clipMaxX = (this.props.clipRect.z - clipOffX) * clipScaleX;
                    final float clipMaxY = (this.props.clipRect.w - clipOffY) * clipScaleY;

                    if (clipMaxX <= clipMinX || clipMaxY <= clipMinY) {
                        continue;
                    }

                    // Apply scissor/clipping rectangle (Y is inverted in OpenGL)
                    renderPass.enableScissor((int) clipMinX, (int) (fbHeight - clipMaxY), (int) (clipMaxX - clipMinX), (int) (clipMaxY - clipMinY));

                    // Bind texture, Draw
                    final long textureId = drawData.getCmdListCmdBufferTextureId(n, cmdIdx);
                    final int vtxOffset = drawData.getCmdListCmdBufferVtxOffset(n, cmdIdx);
                    final int idxOffset = drawData.getCmdListCmdBufferIdxOffset(n, cmdIdx);
                    final int elemCount = drawData.getCmdListCmdBufferElemCount(n, cmdIdx);

                    if (textureId == 0) {
                        throw new IllegalStateException("Texture ID is 0");
                    }

//? if <=1.21.10 {
                    renderPass.bindSampler("Texture", textureId == 1 ? this.data.fontTextureView : this.data.textures.get((int) (textureId - 2)));
//?} else {
                    /^final com.mojang.blaze3d.textures.GpuSampler sampler = RenderSystem.getSamplerCache().getSampler(
                            com.mojang.blaze3d.textures.AddressMode.CLAMP_TO_EDGE,
                            com.mojang.blaze3d.textures.AddressMode.CLAMP_TO_EDGE,
                            com.mojang.blaze3d.textures.FilterMode.LINEAR,
                            com.mojang.blaze3d.textures.FilterMode.LINEAR,
                            true);
                    renderPass.bindTexture("Texture", textureId == 1 ? this.data.fontTextureView : this.data.textures.get((int) (textureId - 2)), sampler);
^///?}
                    renderPass.drawIndexed(vtxOffset, idxOffset, elemCount, 1);
                }
            }
        }

        for (final MappableRingBuffer buffer : this.data.vertexData) {
            buffer.rotate();
        }
        for (final MappableRingBuffer buffer : this.data.indexData) {
            buffer.rotate();
        }
        this.clearTextures();
    }

    @Override
    public void free() {
        final ImGuiIO io = ImGui.getIO();

        this.shutdownPlatformInterface();
        this.destroyDeviceObjects();

        io.setBackendRendererName(null);
        io.removeBackendFlags(ImGuiBackendFlags.RendererHasVtxOffset | ImGuiBackendFlags.RendererHasViewports);
        this.data = null;
    }

    @Override
    public void newFrame() {
        if (this.data.fontTexture == null) {
            this.createFontsTexture();
        }
    }

    @Override
    public void renderDrawData(final ImDrawData drawData) {
        this.renderDrawData(drawData, OptionalInt.empty());
    }

    @Override
    public void recreateFontsTexture() {
        this.destroyFontsTexture();
        this.createFontsTexture();
    }

    @Override
    public long getImGuiId(final ImGuiTextureProvider texture) {
        final GpuTextureView view = switch (texture) {
            case final AbstractTexture abstractTexture -> abstractTexture.getTextureView();
            case final GpuTextureView gpuTextureView -> gpuTextureView;
            default -> throw new IllegalArgumentException("Unexpected value: " + texture);
        };
        long id = ((ImGuiTextureProvider) view).imguimc$id();

        if (id == 0) {
            id = ((ImGuiRenderImplRenderSystem) ImGuiMCImpl.handler.getRenderer()).addTexture(view);
            ((ImGuiTextureProvider) view).imguimc$setId(id);
        }

        return id;
    }

    public void createFontsTexture() {
        final ImFontAtlas fontAtlas = ImGui.getIO().getFonts();

        // Build texture atlas
        // Load as RGBA 32-bit (75% of the memory is wasted, but default font is so small) because it is more likely to be compatible with user's existing shaders.
        // If your ImTextureId represent a higher-level concept than just a GL texture id, consider calling GetTexDataAsAlpha8() instead to save on GPU memory.
        final ImInt width = new ImInt();
        final ImInt height = new ImInt();
//? if >=1.21.9 {
        /^final java.nio.ByteBuffer pixels = fontAtlas.getTexDataAsRGBA32(width, height);
         ^///?} else {
        final java.nio.IntBuffer pixels = fontAtlas.getTexDataAsRGBA32(width, height).asIntBuffer();
//?}

        // TODO use GetTexDataAsAlpha8 instead

        final GpuDevice device = RenderSystem.getDevice();
        this.data.fontTexture = device.createTexture(
                "ImGui Font Atlas",
                GpuTexture.USAGE_COPY_DST | GpuTexture.USAGE_TEXTURE_BINDING,
                TextureFormat.RGBA8,
                width.get(),
                height.get(),
                1,
                1);
        device.createCommandEncoder().writeToTexture(
                this.data.fontTexture,
                pixels,
                NativeImage.Format.RGBA,
                0,
                0,
                0,
                0,
                width.get(),
                height.get()
        );
        this.data.fontTextureView = device.createTextureView(this.data.fontTexture);

        // Store our identifier
        fontAtlas.setTexID(1);
    }

    public void destroyFontsTexture() {
        final ImGuiIO io = ImGui.getIO();
        if (this.data.fontTextureView != null) {
            this.data.fontTextureView.close();
            this.data.fontTextureView = null;
        }
        if (this.data.fontTexture != null) {
            this.data.fontTexture.close();
            io.getFonts().setTexID(0);
            this.data.fontTexture = null;
        }
    }

    private void destroyDeviceObjects() {
        this.data.pipelineHandle = null;
        if (this.data.projectionMatrixBuffer != null) {
            this.data.projectionMatrixBuffer.close();
            this.data.projectionMatrixBuffer = null;
        }
        this.clearVertexData(0);
        this.destroyFontsTexture();
    }

    //--------------------------------------------------------------------------------------------------------
    // MULTI-VIEWPORT / PLATFORM INTERFACE SUPPORT
    // This is an _advanced_ and _optional_ feature, allowing the backend to create and handle multiple viewports simultaneously.
    // If you are new to dear imgui or creating a new binding for dear imgui, it is recommended that you completely ignore this section first..
    //--------------------------------------------------------------------------------------------------------

    private final class RendererRenderWindowFunction extends ImPlatformFuncViewport {
        @Override
        public void accept(final ImGuiViewport vp) {
            ImGuiRenderImplRenderSystem.this.renderDrawData(vp.getDrawData(), !vp.hasFlags(ImGuiViewportFlags.NoRendererClear) ? OptionalInt.of(0) : OptionalInt.empty());
        }
    }

    protected void initPlatformInterface() {
        ImGui.getPlatformIO().setRendererRenderWindow(new RendererRenderWindowFunction());
    }

    protected void shutdownPlatformInterface() {
        ImGui.destroyPlatformWindows();
    }
}
*///?}