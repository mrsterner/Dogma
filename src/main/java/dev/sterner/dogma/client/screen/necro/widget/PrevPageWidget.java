package dev.sterner.dogma.client.screen.necro.widget;

import dev.sterner.dogma.api.IInactiveButton;
import dev.sterner.dogma.client.screen.necro.BookOfTheDeadScreen;
import dev.sterner.dogma.client.screen.necro.tab.BookTab;
import dev.sterner.dogma.client.screen.necro.tab.BotDTab;

public class PrevPageWidget extends PulseWidget implements IInactiveButton {

    public PrevPageWidget(int x, int y, BotDTab tab, BookOfTheDeadScreen screen, int page) {
        super(x, y, 273 + 19, 0, 18, 9, tab, screen, page);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (isValidClickButton(0)) {
            if (tab instanceof BookTab bookTab) {
                screen.clearWidgets();
                bookTab.previousPage();
                screen.initialize();
            }
        }
    }

    @Override
    public boolean isInactive() {
        return !(tab.grouping > 0);
    }
}