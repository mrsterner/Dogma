package dev.sterner.dogma.client.screen.necro.widget;

import dev.sterner.dogma.client.screen.necro.BookOfTheDeadScreen;
import dev.sterner.dogma.client.screen.necro.tab.BotDTab;
import dev.sterner.dogma.client.screen.necro.tab.MainTab;

public class BackPageWidget extends PulseWidget {

    public BackPageWidget(int x, int y, BotDTab tab, BookOfTheDeadScreen screen, int page) {
        super(x, y, 273, 10, 18, 12, tab, screen, page);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (isValidClickButton(0)) {
            screen.clearWidgets();
            screen.tab = new MainTab(screen);
            screen.initialize();
        }
    }
}