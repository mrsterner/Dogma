package dev.sterner.dogma.client.screen.necro.tab;

import dev.sterner.dogma.client.screen.necro.BookEntry;
import dev.sterner.dogma.client.screen.necro.BookOfTheDeadScreen;
import dev.sterner.dogma.client.screen.necro.page.HeadlineBookPage;

public class NecroTab extends BookTab {
    public NecroTab(BookOfTheDeadScreen screen) {
        super(screen, null);
    }

    @Override
    public void init() {
        ENTRIES.add(BookEntry.of()
                .addPage(HeadlineBookPage.of("necro", "necro.1"))
                .addPage(HeadlineBookPage.of("necro.2"))
                .addPage(HeadlineBookPage.of("necro.3"))
        );
    }
}