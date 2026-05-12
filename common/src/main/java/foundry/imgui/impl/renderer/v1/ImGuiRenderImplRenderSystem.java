package foundry.imgui.impl.renderer.v1;

//? if >=1.21.6 {

/*import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.CompiledRenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.shaders.ShaderType;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import foundry.imgui.api.ImGuiMC;
import foundry.imgui.api.ImGuiSampler;
import foundry.imgui.api.ImGuiTextureProvider;
import foundry.imgui.impl.ImGuiMCImpl;
import foundry.imgui.impl.renderer.ImGuiRenderer;
import imgui.*;
import imgui.callback.ImPlatformFuncViewport;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiViewportFlags;
import imgui.type.ImInt;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import java.util.*;

@ApiStatus.Internal
public class ImGuiRenderImplRenderSystem implements ImGuiRenderer {

    private static final VertexFormat VERTEX_FORMAT;

    static {
        //? if >=26.2-snapshot-6 {
        /^VERTEX_FORMAT = VertexFormat.builder(0)
                .addAttribute("Position", com.mojang.blaze3d.GpuFormat.RG32_FLOAT)
                .addAttribute("UV", com.mojang.blaze3d.GpuFormat.RG32_FLOAT)
                .addAttribute("Color", com.mojang.blaze3d.GpuFormat.RGBA8_UNORM)
                .build();
        ^///? } else {

        com.mojang.blaze3d.vertex.VertexFormatElement posElement = null;
        for (int i = 7; i < com.mojang.blaze3d.vertex.VertexFormatElement.MAX_COUNT; i++) {
            final com.mojang.blaze3d.vertex.VertexFormatElement element = com.mojang.blaze3d.vertex.VertexFormatElement.byId(i);
            if (element == null) {
                //? if >=26.2-snapshot-1 {
                /^posElement = com.mojang.blaze3d.vertex.VertexFormatElement.register(i, 0, com.mojang.blaze3d.GpuFormat.RG32_FLOAT);
                 ^///? } else if >= 26.1 {
                /^posElement = com.mojang.blaze3d.vertex.VertexFormatElement.register(i, 0, com.mojang.blaze3d.vertex.VertexFormatElement.Type.FLOAT, false, 2);
                ^///? } else {
                posElement = com.mojang.blaze3d.vertex.VertexFormatElement.register(i, 0, com.mojang.blaze3d.vertex.VertexFormatElement.Type.FLOAT, com.mojang.blaze3d.vertex.VertexFormatElement.Usage.POSITION, 2);
                 //? }
                break;
            }
        }

        if (posElement == null) {
            throw new IllegalStateException("Failed to create vertex format");
        }

        VERTEX_FORMAT = VertexFormat.builder()
                .add("Position", posElement)
                .add("UV", com.mojang.blaze3d.vertex.VertexFormatElement.UV0)
                .add("Color", com.mojang.blaze3d.vertex.VertexFormatElement.COLOR)
                .build();
        //? }
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
            //? if >=26.2-snapshot-3 {
            /^.withBindGroupLayout(com.mojang.blaze3d.pipeline.BindGroupLayout.builder()
                    .withSampler("Texture")
                    .withUniform("Projection", UniformType.UNIFORM_BUFFER)
                    .build())
            ^///? } else {
            .withSampler("Texture")
            .withUniform("Projection", UniformType.UNIFORM_BUFFER)
            //? }
            //? if >=26.2-snapshot-6 {
            /^.withColorTargetState(new com.mojang.blaze3d.pipeline.ColorTargetState(BlendFunction.TRANSLUCENT))
             ^///? } else if >=26.1 {
            /^.withColorTargetState(new com.mojang.blaze3d.pipeline.ColorTargetState(Optional.of(BlendFunction.TRANSLUCENT), com.mojang.blaze3d.pipeline.ColorTargetState.WRITE_ALL))
            ^///? } else {
            .withBlend(BlendFunction.TRANSLUCENT)
             //? }
            .withCull(false)
            //? if >=26.2-snapshot-6 {
            /^.withVertexBinding(0, VERTEX_FORMAT)
            .withPrimitiveTopology(com.mojang.blaze3d.PrimitiveTopology.TRIANGLES)
            ^///? } else {
            .withVertexFormat(VERTEX_FORMAT, VertexFormat.Mode.TRIANGLES)
            //? }
            .build();

    private static String getShaderSource(final ResourceLocation name, final ShaderType shaderType) {
        return SOURCES_MAP.get(name);
    }

    //? if >= 1.21.11 {
    /^public long addTexture(final GpuTextureView view, @Nullable final com.mojang.blaze3d.textures.GpuSampler sampler) {
        this.data.textures.add(view);
        this.data.samplers.add(sampler);
        return this.data.textures.size() + 1;
    }
    ^///? } else {
    public long addTexture(final GpuTextureView view) {
        this.data.textures.add(view);
        return this.data.textures.size() + 1;
    }
    //? }

    /^*
     * Data class to store implementation specific fields.
     * Same as {@code ImGui_ImplOpenGL3_Data}.
     ^/
    protected static class Data {
        protected GpuTextureView fontTextureView;
        protected GpuTexture fontTexture;
        protected CompiledRenderPipeline pipelineHandle;
        protected CachedImguiOrthoBuffer projectionMatrixBuffer;
        protected List<GpuBuffer> vertexData = new ArrayList<>();
        protected List<GpuBuffer> indexData = new ArrayList<>();
        protected int elementSize;
        protected List<GpuTextureView> textures = new ArrayList<>();
        //? if >= 1.21.11 {
        /^protected List<com.mojang.blaze3d.textures.GpuSampler> samplers = new ArrayList<>();
        ^///? }
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
        //? if >= 1.21.11 {
        /^this.data.samplers.clear();
        ^///? }

        if (this.data.textures.isEmpty()) {
            return;
        }

        final Iterator<GpuTextureView> iterator = this.data.textures.iterator();
        while (iterator.hasNext()) {
            ImGuiMC.getTexture(iterator.next()).imguimc$setId(0);
            iterator.remove();
        }
    }

    private void clearVertexData(final int maxCommands) {
        final int removeVertices = this.data.vertexData.size() - maxCommands;
        if (removeVertices > 0) {
            final Iterator<GpuBuffer> iterator = this.data.vertexData.iterator();
            for (int i = 0; i < removeVertices; i++) {
                iterator.next().close();
                iterator.remove();
            }
        }

        final int removeIndices = this.data.indexData.size() - maxCommands;
        if (removeIndices > 0) {
            final Iterator<GpuBuffer> iterator = this.data.indexData.iterator();
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
        //? if <=26.1 {
        io.setBackendRendererName("imgui-java_impl_" + device.getBackendName());
        //? } else {
        /^io.setBackendRendererName("imgui-java_impl_" + device.getDeviceInfo().backendName());
         ^///? }

        // We can honor the ImDrawCmd::VtxOffset field, allowing for large meshes.
        io.addBackendFlags(ImGuiBackendFlags.RendererHasVtxOffset);

        // We can create multi-viewports on the Renderer side (optional)
        io.addBackendFlags(ImGuiBackendFlags.RendererHasViewports);

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            this.initPlatformInterface();
        }
    }

    //? if >=26.2-snapshot-6 {
    /^@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private void renderDrawData(final ImDrawData drawData, final RenderTarget renderTarget, final Optional<org.joml.Vector4fc> clearColor) {
    ^///? } else {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private void renderDrawData(final ImDrawData drawData, final RenderTarget renderTarget, final OptionalInt clearColor) {
        //? }

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
            final Iterator<GpuBuffer> iterator = this.data.indexData.iterator();
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
            final GpuBuffer vertexBuffer;
            final int vertexBufferSize = drawData.getCmdListVtxBufferSize(n) * ImDrawData.sizeOfImDrawVert();

            if (n >= this.data.vertexData.size()) {
                final int index = n;
                vertexBuffer = device.createBuffer(() -> "ImGui Vertex Buffer " + index, GpuBuffer.USAGE_COPY_DST | GpuBuffer.USAGE_VERTEX, vertexBufferSize);
                this.data.vertexData.add(vertexBuffer);
            } else {
                final GpuBuffer buffer = this.data.vertexData.get(n);

                if (buffer.size() >= vertexBufferSize) {
                    vertexBuffer = buffer;
                } else {
                    buffer.close();
                    final int index = n;
                    vertexBuffer = device.createBuffer(() -> "ImGui Vertex Buffer " + index, GpuBuffer.USAGE_COPY_DST | GpuBuffer.USAGE_VERTEX, vertexBufferSize);
                    this.data.vertexData.set(n, vertexBuffer);
                }
            }

            final GpuBuffer indexBuffer;
            final int indexBufferSize = drawData.getCmdListIdxBufferSize(n) * this.data.elementSize;

            if (n >= this.data.indexData.size()) {
                final int index = n;
                indexBuffer = device.createBuffer(() -> "ImGui Index Buffer " + index, GpuBuffer.USAGE_COPY_DST | GpuBuffer.USAGE_INDEX, indexBufferSize);
                this.data.indexData.add(indexBuffer);
            } else {
                final GpuBuffer buffer = this.data.indexData.get(n);

                if (buffer.size() >= indexBufferSize) {
                    indexBuffer = buffer;
                } else {
                    buffer.close();
                    final int index = n;
                    indexBuffer = device.createBuffer(() -> "ImGui Index Buffer " + index, GpuBuffer.USAGE_COPY_DST | GpuBuffer.USAGE_INDEX, indexBufferSize);
                    this.data.indexData.set(n, indexBuffer);
                }
            }

            commandEncoder.writeToBuffer(vertexBuffer.slice(0, vertexBufferSize), drawData.getCmdListVtxBufferData(n));
            commandEncoder.writeToBuffer(indexBuffer.slice(0, indexBufferSize), drawData.getCmdListIdxBufferData(n));
        }

        // TODO viewport

        final GpuBufferSlice projectionMatrixBuffer = this.data.projectionMatrixBuffer.getBuffer(L, R, B, T);

        try (final RenderPass renderPass = commandEncoder.createRenderPass(
                () -> "ImGui",
                renderTarget.getColorTextureView(),
                clearColor,
                renderTarget.getDepthTextureView(),
                OptionalDouble.empty())) {
            renderPass.setPipeline(PIPELINE);
            renderPass.setUniform("Projection", projectionMatrixBuffer);

            // Render command lists
            for (int n = 0; n < cmdListsCount; n++) {
                final GpuBuffer vertexBuffer = this.data.vertexData.get(n);
                final GpuBuffer indexBuffer = this.data.indexData.get(n);

                //? if >=26.2-snapshot-6 {
                /^renderPass.setVertexBuffer(0, vertexBuffer.slice());
                renderPass.setIndexBuffer(indexBuffer, this.data.elementSize == 2 ? com.mojang.blaze3d.IndexType.SHORT : com.mojang.blaze3d.IndexType.INT);
                ^///? } else {
                renderPass.setVertexBuffer(0, vertexBuffer);
                renderPass.setIndexBuffer(indexBuffer, this.data.elementSize == 2 ? VertexFormat.IndexType.SHORT : VertexFormat.IndexType.INT);
                //? }

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

                    //? if >=1.21.11 {
                    /^final com.mojang.blaze3d.textures.GpuSampler defaultSampler = RenderSystem.getSamplerCache().getSampler(
                            com.mojang.blaze3d.textures.AddressMode.CLAMP_TO_EDGE,
                            com.mojang.blaze3d.textures.AddressMode.CLAMP_TO_EDGE,
                            com.mojang.blaze3d.textures.FilterMode.LINEAR,
                            com.mojang.blaze3d.textures.FilterMode.LINEAR,
                            true);
                    final com.mojang.blaze3d.textures.GpuSampler sampler = textureId == 1 ? defaultSampler : this.data.samplers.get((int) (textureId - 2));
                    renderPass.bindTexture("Texture", textureId == 1 ? this.data.fontTextureView : this.data.textures.get((int) (textureId - 2)), sampler != null ? sampler : defaultSampler);
                    ^///?} else {
                    renderPass.bindSampler("Texture", textureId == 1 ? this.data.fontTextureView : this.data.textures.get((int) (textureId - 2)));
                     //?}

                    //? if >=26.2-snapshot-7 {
                    /^renderPass.drawIndexed(elemCount, 1, idxOffset, vtxOffset, 0);
                     ^///? } else {
                    renderPass.drawIndexed(vtxOffset, idxOffset, elemCount, 1);
                    //? }
                }
            }
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
    public void renderDrawData(final ImDrawData drawData, final RenderTarget renderTarget) {
        //? if >=26.2-snapshot-6 {
        /^this.renderDrawData(drawData, renderTarget, Optional.empty());
         ^///? } else {
        this.renderDrawData(drawData, renderTarget, OptionalInt.empty());
        //? }
    }

    @Override
    public void recreateFontsTexture() {
        this.destroyFontsTexture();
        this.createFontsTexture();
    }

    @Override
    public long getImGuiId(final ImGuiTextureProvider texture, @Nullable final ImGuiSampler sampler) {
        long id = texture.imguimc$id();

        if (id == 0) {
            final GpuTextureView view = switch (texture) {
                case final AbstractTexture abstractTexture -> abstractTexture.getTextureView();
                case final GpuTextureView gpuTextureView -> gpuTextureView;
                default -> throw new IllegalArgumentException("Unexpected value: " + texture);
            };
            //? if >= 1.21.11 {
            /^id = ((ImGuiRenderImplRenderSystem) ImGuiMCImpl.handler.getRenderer()).addTexture(view, (com.mojang.blaze3d.textures.GpuSampler) sampler);
            ^///? } else {
            id = ((ImGuiRenderImplRenderSystem) ImGuiMCImpl.handler.getRenderer()).addTexture(view);
             //? }
            texture.imguimc$setId(id);
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
                //? if <= 26.1 {
                com.mojang.blaze3d.textures.TextureFormat.RGBA8,
                //? } else {
                /^com.mojang.blaze3d.GpuFormat.RGBA8_UNORM,
                 ^///? }
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

        //? if >=26.2-snapshot-6 {
        /^private static final org.joml.Vector4fc CLEAR_COLOR = new org.joml.Vector4f();
         ^///? }

        @Override
        public void accept(final ImGuiViewport vp) {
            //? if >=26.2-snapshot-6 {
            /^final Optional<org.joml.Vector4fc> clearColor = !vp.hasFlags(ImGuiViewportFlags.NoRendererClear) ? Optional.of(CLEAR_COLOR) : Optional.empty();
             ^///? } else {
            final OptionalInt clearColor = !vp.hasFlags(ImGuiViewportFlags.NoRendererClear) ? OptionalInt.of(0) : OptionalInt.empty();
            //? }
            ImGuiRenderImplRenderSystem.this.renderDrawData(vp.getDrawData(), ImGuiMCImpl.getMainRenderTarget(), clearColor);
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