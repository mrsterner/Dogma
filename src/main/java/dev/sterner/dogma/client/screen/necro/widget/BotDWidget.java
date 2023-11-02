package dev.sterner.dogma.client.screen.necro.widget;

import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class BotDWidget extends AbstractWidget {
    public int page;

    public BotDWidget(int x, int y, int width, int height, Component message, int page) {
        super(x, y, width, height, message);
        this.page = page;
    }

    public void tick() {
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }
}