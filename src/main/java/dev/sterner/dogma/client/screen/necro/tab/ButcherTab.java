package dev.sterner.dogma.client.screen.necro.tab;

import dev.sterner.dogma.client.screen.necro.BookEntry;
import dev.sterner.dogma.client.screen.necro.BookOfTheDeadScreen;
import dev.sterner.dogma.client.screen.necro.page.HeadlineBookPage;

public class ButcherTab extends BookTab {
    public ButcherTab(BookOfTheDeadScreen screen) {
        super(screen, null);
    }

    @Override
    public void init() {
        ENTRIES.add(BookEntry.of()
                .addPage(HeadlineBookPage.of("butcher", "butcher.1"))
                .addPage(HeadlineBookPage.of("butcher.2"))
                .addPage(HeadlineBookPage.of("butcher.3"))
        );
    }
}