package dev.sterner.dogma.client.screen.necro.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.sterner.dogma.api.knowledge.Knowledge;
import dev.sterner.dogma.api.knowledge.KnowledgeData;
import dev.sterner.dogma.client.screen.necro.tab.KnowledgeTab;
import dev.sterner.dogma.foundation.DogmaPackets;
import dev.sterner.dogma.foundation.capability.necro.NecroPlayerDataCapability;
import dev.sterner.dogma.foundation.networking.KnowledgeC2SPacket;
import dev.sterner.dogma.foundation.networking.SyncPlayerCapabilityDataPacket;
import dev.sterner.dogma.foundation.util.RenderUtils;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;
import java.util.Set;

import static com.mojang.blaze3d.systems.RenderSystem.disableScissor;
import static com.mojang.blaze3d.systems.RenderSystem.enableScissor;

public class KnowledgeWidget extends BotDWidget {
    public Knowledge knowledge;
    public KnowledgeTab tab;
    public float x;
    public float y;
    public Set<KnowledgeData> knowledgeDataList;

    public KnowledgeWidget(float x, float y, KnowledgeTab tab, Knowledge knowledge, int page) {
        super((int) x, (int) y, 17, 17, GameNarrator.NO_TITLE, page);
        this.tab = tab;
        this.knowledge = knowledge;
        this.x = x;
        this.y = y;
        this.knowledgeDataList = NecroPlayerDataCapability.getCapability(tab.player).getKnowledgeData();
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        List<Knowledge> knowledgeList = knowledgeDataList.stream().map(KnowledgeData::knowledge).toList();
        boolean isActivated = knowledgeList.contains(knowledge);
        boolean shouldRender = isActive(knowledgeList);

        if (shouldRender) {
            RenderSystem.setShaderTexture(0, knowledge.icon);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, isHovered() || isActivated ? this.alpha : 0.25F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            // Render the screen texture
            PoseStack matrices = guiGraphics.pose();
            guiGraphics.enableScissor((tab.width - 192) / 4 - 5, tab.scissorY + 13, tab.scissorWidth + (tab.width - 192) / 4 - 5 - 61, tab.scissorHeight + 1);
            matrices.pushPose();
            matrices.translate(tab.xOffset, tab.yOffset, 0.0F);
            RenderUtils.drawTexture(guiGraphics, this.x + (float) tab.xOffset, this.y + (float) tab.yOffset, 0, 0, this.width, this.height, 17, 17);
            matrices.popPose();
            guiGraphics.disableScissor();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.isHovered = mouseX >= this.getX() + tab.xOffset * 2 && mouseY >= this.getY() + tab.yOffset * 2 && mouseX < this.getX() + tab.xOffset * 2 + this.width && mouseY < this.getY() + tab.yOffset * 2 + this.height;
        this.renderWidget(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        if (!this.active && !this.visible) {
            return false;
        }

        List<Knowledge> knowledgeList = knowledgeDataList.stream().map(KnowledgeData::knowledge).toList();
        if (isActive(knowledgeList)) {
            double halfWidth = (double) this.width / 2;
            double halfHeight = (double) this.height / 2;
            double centerX = this.getX() + halfWidth + tab.xOffset * 2;
            double centerY = this.getY() + halfHeight + tab.yOffset * 2;
            double radius = Math.min(halfWidth, halfHeight);

            double dx = Math.abs(mouseX - centerX);
            double dy = Math.abs(mouseY - centerY);

            if (dx > radius || dy > radius) {
                return false;
            }
            return dx + dy <= radius;
        }
        return false;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (isValidClickButton(0)) {
            DogmaPackets.DOGMA_CHANNEL.sendToServer(new KnowledgeC2SPacket(knowledge));
        }
    }

    public boolean isActive(List<Knowledge> knowledgeList) {
        boolean shouldRender = true;

        if (!knowledge.children.isEmpty()) {
            for (Knowledge child : knowledge.children) {
                if (!knowledgeList.contains(child)) {
                    shouldRender = false;
                    break;
                }
            }
        }
        return shouldRender;
    }
}