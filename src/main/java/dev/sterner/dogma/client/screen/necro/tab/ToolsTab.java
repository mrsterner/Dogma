package dev.sterner.dogma.client.screen.necro.tab;

import dev.sterner.dogma.client.screen.necro.BookEntry;
import dev.sterner.dogma.client.screen.necro.BookOfTheDeadScreen;
import dev.sterner.dogma.client.screen.necro.page.HeadlineBookPage;
import dev.sterner.dogma.client.screen.necro.page.TextPage;

public class ToolsTab extends BookTab {
    public ToolsTab(BookOfTheDeadScreen screen) {
        super(screen, null);
    }

    @Override
    public void init() {
        ENTRIES.add(BookEntry.of()
                .addPage(HeadlineBookPage.of("tools", "tools.1"))
                .addPage(TextPage.of("tools.2"))
                .addPage(TextPage.of("tools.3"))
        );
    }
}