package dev.sterner.dogma.client.screen.necro.tab;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.client.screen.necro.BookEntry;
import dev.sterner.dogma.client.screen.necro.BookOfTheDeadScreen;
import dev.sterner.dogma.client.screen.necro.page.HeadlineBookPage;
import dev.sterner.dogma.client.screen.necro.page.TextPage;
import dev.sterner.dogma.client.screen.necro.widget.KnowledgeWidget;
import dev.sterner.dogma.foundation.registry.DogmaRegistries;
import dev.sterner.dogma.foundation.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.List;

import static com.mojang.blaze3d.systems.RenderSystem.disableScissor;
import static com.mojang.blaze3d.systems.RenderSystem.enableScissor;
import static dev.sterner.dogma.foundation.util.RenderUtils.drawTexture;

public class KnowledgeTab extends BookTab {
    public static final ResourceLocation TAB_TEXTURE = Dogma.id("textures/gui/knowledge_tab.png");
    public final Player player;
    public static final ResourceLocation PARALLAX = Dogma.id("textures/gui/parallax.png");
    public double xOffset = 0;
    public double yOffset = 0;
    public int scissorX;
    public int scissorY;
    public int scissorWidth;
    public int scissorHeight;

    public KnowledgeTab(BookOfTheDeadScreen screen) {
        super(screen, List.of(Dogma.id("textures/gui/background/knowledge_tab.png")));
        this.player = screen.player;
        this.scissorX = 50;
        this.scissorY = 30;
        this.scissorWidth = 122 + scissorX;
        this.scissorHeight = 172 + scissorY;
    }

    @Override
    public void init() {
        ENTRIES.add(BookEntry.of()
                .addPage(TextPage.of())
                .addPage(HeadlineBookPage.of("knowledge", "knowledge.1"))
                .addPage(HeadlineBookPage.of("knowledge.2"))
        );

        float x = (float) (width - 192) / 4 + 9 * 5 - 4;
        float y = 18 * 6 + 10;
        //Void
        widgets.add(new KnowledgeWidget(x - 9, y + 15, this, DogmaRegistries.VOID.get(), 0));
        widgets.add(new KnowledgeWidget(x + 9, y + 15, this, DogmaRegistries.AMALGAM.get(), 0));
        widgets.add(new KnowledgeWidget(x, y + 12 + 18, this, DogmaRegistries.DISPOSITION.get(), 0));
        widgets.add(new KnowledgeWidget(x, y + 12 + 18 * 2, this, DogmaRegistries.BALANCE.get(), 0));
        widgets.add(new KnowledgeWidget(x, y + 12 + 18 * 3, this, DogmaRegistries.PROJECTION.get(), 0));
        //Core
        widgets.add(new KnowledgeWidget(x, y, this, DogmaRegistries.ALCHEMY.get(), 0));
        //Main
        widgets.add(new KnowledgeWidget(x - 9, y - 15, this, DogmaRegistries.CALCINATION.get(), 0));
        widgets.add(new KnowledgeWidget(x + 9, y - 15, this, DogmaRegistries.DISSOLUTION.get(), 0));
        widgets.add(new KnowledgeWidget(x, y - 12 - 18, this, DogmaRegistries.SEPARATION.get(), 0));
        widgets.add(new KnowledgeWidget(x + 9, y - 15 - 30, this, DogmaRegistries.CONJUNCTION.get(), 0));
        widgets.add(new KnowledgeWidget(x - 9, y - 15 - 30, this, DogmaRegistries.FERMENTATION.get(), 0));
        widgets.add(new KnowledgeWidget(x, y - 12 - 18 - 30, this, DogmaRegistries.DISTILLATION.get(), 0));
        widgets.add(new KnowledgeWidget(x, y - 12 - 18 * 2 - 30, this, DogmaRegistries.COAGULATION.get(), 0));
        widgets.add(new KnowledgeWidget(x, y - 12 - 18 * 3 - 30, this, DogmaRegistries.PHILOSOPHER.get(), 0));
        //Soul
        widgets.add(new KnowledgeWidget(x + 18, y, this, DogmaRegistries.SOUL.get(), 0));
        widgets.add(new KnowledgeWidget(x + 9 + 18, y - 15, this, DogmaRegistries.ESSENCE.get(), 0));
        widgets.add(new KnowledgeWidget(x + 18, y - 12 - 18, this, DogmaRegistries.FUSION.get(), 0));
        widgets.add(new KnowledgeWidget(x + 9 + 18, y - 15 - 30, this, DogmaRegistries.MULTIPLICATION.get(), 0));
        widgets.add(new KnowledgeWidget(x + 18 + 18, y - 12 - 18 - 30, this, DogmaRegistries.LIFE.get(), 0));
        //Infernal
        widgets.add(new KnowledgeWidget(x - 18, y, this, DogmaRegistries.BRIMSTONE.get(), 0));
        widgets.add(new KnowledgeWidget(x - 9 - 18, y - 15, this, DogmaRegistries.ASH.get(), 0));
        widgets.add(new KnowledgeWidget(x - 18, y - 12 - 18, this, DogmaRegistries.MELT.get(), 0));
        widgets.add(new KnowledgeWidget(x - 9 - 18, y - 15 - 30, this, DogmaRegistries.ROT.get(), 0));
        widgets.add(new KnowledgeWidget(x - 18 - 18, y - 12 - 18 - 30, this, DogmaRegistries.CADUCEUS.get(), 0));
    }

    @Override
    public boolean move(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.grouping != 0) {
            return false;
        }

        if (!this.isDragging) {
            // Check if the mouse is within the screen texture
            if (mouseX >= (double) (this.width - 192) / 4 - 16 && mouseX <= (double) (this.width - 192) / 4 - 16 + 122 && mouseY >= this.yOffset && mouseY <= this.scissorHeight) {
                this.isDragging = true;
            }
        }

        if (this.isDragging) {
            // Update the position of the screen
            this.xOffset = Mth.clamp(this.xOffset + deltaX, -20, 20);
            this.yOffset = Mth.clamp(this.yOffset + deltaY, -20, 20);


            return true;
        }
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int width, int mouseX, int mouseY, float delta) {
        int scaledWidth = (width - 192) / 4 - 5;

        if (this.grouping == 0) {
            PoseStack matrices = guiGraphics.pose();
            matrices.pushPose();
            RenderUtils.renderTexture(guiGraphics, TAB_TEXTURE, (width - 192) / 4 - 16, 32, 0, 0, 272, 182, 512, 256);
            matrices.popPose();

            RenderSystem.setShaderTexture(0, PARALLAX);
            //enableScissor((width - 192) / 4 - 16, 32, 272, 182);
            matrices.pushPose();

            guiGraphics.enableScissor((width - 192) / 4 - 5, scissorY + 13, scissorWidth + (width - 192) / 4 - 5 - 61, scissorHeight + 1);
            matrices.translate(xOffset, yOffset, 0.0F);

            //drawTexture(guiGraphics, 0, 0, 300.0F, 200.0F, 2507, 1205, 2507, 1205);
            guiGraphics.blit(PARALLAX, 0, 0, 300.0F, 200.0F, 2507, 1205, 2507, 1205);

            matrices.popPose();
            guiGraphics.disableScissor();
        }

        super.render(guiGraphics, width, mouseX, mouseY, delta);
    }
}