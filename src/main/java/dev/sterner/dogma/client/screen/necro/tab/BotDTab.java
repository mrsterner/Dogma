package dev.sterner.dogma.client.screen.necro.tab;

import dev.sterner.dogma.client.screen.necro.BookOfTheDeadScreen;
import dev.sterner.dogma.client.screen.necro.widget.BotDWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.intellij.lang.annotations.Identifier;

import java.util.ArrayList;
import java.util.List;

public class BotDTab {
    public BookOfTheDeadScreen screen;
    public int width;
    public boolean isDragging;
    public List<ResourceLocation> background;
    public List<BotDWidget> widgets = new ArrayList<>();
    public int grouping;

    public BotDTab(BookOfTheDeadScreen screen, List<ResourceLocation> background) {
        this.screen = screen;
        this.background = background;
    }

    public void tick() {
        for (BotDWidget botDWidget : widgets) {
            botDWidget.tick();
        }
    }

    public void preInit() {
    }

    public void init() {
    }

    public void postInit() {
    }

    public void render(GuiGraphics guiGraphics, int width, int mouseX, int mouseY, float delta) {

    }

    public boolean move(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }
}
