package dev.sterner.dogma.client.screen.necro.widget;

import dev.sterner.dogma.client.screen.necro.BookOfTheDeadScreen;
import dev.sterner.dogma.client.screen.necro.tab.BotDTab;

public class NavigationWidget extends PulseWidget {

    public BotDTab targetTab;

    public NavigationWidget(BotDTab targetTab, int x, int y, int u, int v, BotDTab tab, BookOfTheDeadScreen screen, int page) {
        super((tab.width - 192) / 4 + x, y, u, v, 24, 24, tab, screen, page);
        this.targetTab = targetTab;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (isValidClickButton(0)) {
            screen.clearWidgets();
            screen.tab = targetTab;
            screen.initialize();
        }
    }
}