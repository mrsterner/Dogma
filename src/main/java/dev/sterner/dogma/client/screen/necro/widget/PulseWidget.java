package dev.sterner.dogma.client.screen.necro.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.sterner.dogma.api.IInactiveButton;
import dev.sterner.dogma.client.screen.necro.BookOfTheDeadScreen;
import dev.sterner.dogma.client.screen.necro.tab.BotDTab;
import dev.sterner.dogma.foundation.util.RenderUtils;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.util.Mth;

public class PulseWidget extends BotDWidget {
    public int h;
    public int w;
    public int u;
    public int v;
    public int x;
    public int y;
    public BookOfTheDeadScreen screen;
    public BotDTab tab;
    public int hoverTick = 0;

    public PulseWidget(int x, int y, int u, int v, int w, int h, BotDTab tab, BookOfTheDeadScreen screen, int page) {
        super(x, y, w, h, GameNarrator.NO_TITLE, page);
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        this.u = u;
        this.v = v;
        this.tab = tab;
        this.screen = screen;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

        float pulseAlpha = (Mth.sin(hoverTick / 20f) + 1) / 2;

        boolean bl = this instanceof IInactiveButton ib && ib.isInactive();

        RenderSystem.setShaderTexture(0, BookOfTheDeadScreen.BOOK_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, isHovered() && !bl ? this.alpha : 0.85F);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        // Render the screen texture

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        if (isHovered() && !bl) {
            poseStack.translate(-pulseAlpha / 2, -pulseAlpha / 2, 0);
            poseStack.scale(1 + pulseAlpha * 0.005f, 1 + pulseAlpha * 0.005f, 1);
        }
        RenderUtils.renderTexture(guiGraphics, BookOfTheDeadScreen.BOOK_TEXTURE, x, y, u, v, w, h, 512, 256);
        poseStack.popPose();
    }

    @Override
    public boolean isHovered() {
        hoverTick++;
        return super.isHovered();
    }

    @Override
    public void tick() {
        if (!isHovered()) {
            hoverTick = 0;
        }
    }
}