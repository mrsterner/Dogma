package dev.sterner.dogma.foundation.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RenderUtils {

    public static void drawTexture(GuiGraphics guiGraphics, float x, float y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        drawTexturedQuad(guiGraphics, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
    }

    public static void drawTexture(GuiGraphics guiGraphics, float x, float y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        drawTexture(guiGraphics, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
    }

    private static void drawTexturedQuad(GuiGraphics guiGraphics, float x0, float x1, float y0, float y1, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
        drawTexturedQuad(guiGraphics.pose().last().pose(), x0, x1, y0, y1, z, (u + 0.0F) / (float) textureWidth, (u + (float) regionWidth) / (float) textureWidth, (v + 0.0F) / (float) textureHeight, (v + (float) regionHeight) / (float) textureHeight);
    }

    private static void drawTexturedQuad(Matrix4f matrix, float x0, float x1, float y0, float y1, int z, float u0, float u1, float v0, float v1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix, x0, y0, (float) z).uv(u0, v0).endVertex();
        bufferBuilder.vertex(matrix, x0, y1, (float) z).uv(u0, v1).endVertex();
        bufferBuilder.vertex(matrix, x1, y1, (float) z).uv(u1, v1).endVertex();
        bufferBuilder.vertex(matrix, x1, y0, (float) z).uv(u1, v0).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
    }

    public static void renderTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
        guiGraphics.blit(texture, x, y, u, v, width, height, textureWidth, textureHeight);
        //drawTexture(guiGraphics, x, y, u, v, width, height, textureWidth, textureHeight);

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
