package dev.sterner.dogma.client.screen.necro;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.api.AbstractDogmaScreen;
import dev.sterner.dogma.client.screen.necro.tab.BotDTab;
import dev.sterner.dogma.client.screen.necro.tab.MainTab;
import dev.sterner.dogma.client.screen.necro.widget.BackPageWidget;
import dev.sterner.dogma.client.screen.necro.widget.NextPageWidget;
import dev.sterner.dogma.client.screen.necro.widget.PrevPageWidget;
import dev.sterner.dogma.foundation.util.RenderUtils;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class BookOfTheDeadScreen extends AbstractDogmaScreen {
    public static final ResourceLocation BOOK_TEXTURE = Dogma.id("textures/gui/book_of_the_dead.png");
    public Player player;
    public static BookOfTheDeadScreen screen;
    public BotDTab tab;

    protected BookOfTheDeadScreen(Player player) {
        super(GameNarrator.NO_TITLE);
        minecraft = Minecraft.getInstance();
        this.player = player;
        this.tab = new MainTab(this);
    }

    @Override
    protected void init() {
        initialize();
    }

    public void initialize() {
        if (tab != null) {
            tab.width = this.width;
            tab.preInit();
            tab.init();
            tab.postInit();

        }

        int x = (width - 192) / 4 + 9 * 12 + 3;
        int y = 32 * 6 + 1;

        addRenderableWidget(new NextPageWidget(x + 18 * 6 + 7, y, tab, this, -1));
        addRenderableWidget(new PrevPageWidget(x - (18 * 6 + 7), y, tab, this, -1));
        addRenderableWidget(new BackPageWidget(x, y - 5, tab, this, -1));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        PoseStack matrices = guiGraphics.pose();
        matrices.pushPose();
        this.renderBackground(guiGraphics);
        this.setFocused(false);
        RenderUtils.renderTexture(guiGraphics, BOOK_TEXTURE, (this.width - 192) / 4 - 16, 32, 0, 0, 272, 182, 512, 256);
        matrices.popPose();
        if (tab != null) {
            if (tab.background != null) {
                int bgIndex = Mth.ceil(tab.grouping / 2d);
                if (bgIndex >= 0 && tab.background.size() > bgIndex) {
                    RenderUtils.renderTexture(guiGraphics, tab.background.get(bgIndex), (this.width - 192) / 4 - 16, 32, 0, 0, 272, 182, 512, 256);
                }
            }
            tab.render(guiGraphics, this.width, mouseX, mouseY, delta);
        }

        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button == 0) {
            if (tab != null) {
                return tab.move(mouseX, mouseY, button, deltaX, deltaY);
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (tab != null) {
            tab.isDragging = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }


    @Override
    public void tick() {
        if (tab != null) {
            tab.tick();
        }
        super.tick();
    }

    public static void openScreen(Player player) {
        Minecraft.getInstance().setScreen(getInstance(player));
    }

    public static BookOfTheDeadScreen getInstance(Player player) {
        if (screen == null) {
            screen = new BookOfTheDeadScreen(player);
        }
        return screen;
    }
}
