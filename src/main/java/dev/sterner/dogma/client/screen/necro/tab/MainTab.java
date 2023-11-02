package dev.sterner.dogma.client.screen.necro.tab;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.client.screen.necro.BookEntry;
import dev.sterner.dogma.client.screen.necro.BookOfTheDeadScreen;
import dev.sterner.dogma.client.screen.necro.page.HeadlineBookPage;
import dev.sterner.dogma.client.screen.necro.widget.NavigationWidget;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class MainTab extends BookTab {
    public MainTab(BookOfTheDeadScreen screen) {
        super(screen, List.of(Dogma.id("textures/gui/background/main_tab.png")));
    }

    @Override
    public void init() {
        ENTRIES.add(BookEntry.of()
                .addPage(HeadlineBookPage.of("main", "main.1"))
                .addPage(HeadlineBookPage.of("glossary"))
                .addPage(HeadlineBookPage.of("glossary"))
        );

        widgets.add(new NavigationWidget(new ToolsTab(screen), 18 * 7 + 10, 70, 273, 23, this, screen, 0));
        widgets.add(new NavigationWidget(new KnowledgeTab(screen), 18 * 12 + 1, 70 + 25, 273, 23 + 25, this, screen, 0));
        widgets.add(new NavigationWidget(new ButcherTab(screen), 18 * 7 + 10, 70 + 25 * 2, 273, 23 + 25 + 25, this, screen, 0));
        widgets.add(new NavigationWidget(new NecroTab(screen), 18 * 12 + 1, 70 + 25 * 3, 273, 23 + 25 + 25 + 25, this, screen, 0));
    }
}
