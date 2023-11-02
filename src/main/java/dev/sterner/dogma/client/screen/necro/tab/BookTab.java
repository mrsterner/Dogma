package dev.sterner.dogma.client.screen.necro.tab;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import dev.sterner.dogma.client.screen.necro.BookEntry;
import dev.sterner.dogma.client.screen.necro.BookOfTheDeadScreen;
import dev.sterner.dogma.client.screen.necro.page.BookPage;
import dev.sterner.dogma.client.screen.necro.widget.BotDWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class BookTab extends BotDTab {
    public BookEntry openEntry;

    public static List<BookEntry> ENTRIES = new ArrayList<>();

    protected BookTab(BookOfTheDeadScreen screen, List<ResourceLocation> background) {
        super(screen, background);
    }

    @Override
    public void preInit() {
        ENTRIES.clear();
        widgets.clear();
    }

    @Override
    public void postInit() {
        for (BotDWidget widget : widgets) {
            if (grouping == widget.page) {
                screen.addRenderableWidget(widget);
            }

        }
        if (ENTRIES.size() > 0) {
            openEntry = ENTRIES.get(0);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int width, int mouseX, int mouseY, float delta) {
        float x = (float) (width - 192) / 4 + 9 * 5 - 4;
        float y = 18 * 6 + 10;

        if (openEntry != null) {
            if (!openEntry.pages.isEmpty()) {
                int openPages = grouping * 2;
                for (int i = openPages; i < openPages + 2; i++) {
                    if (i < openEntry.pages.size()) {
                        BookPage page = openEntry.pages.get(i);
                        if (i % 2 == 0) {
                            page.renderLeft(screen.getMinecraft(), guiGraphics, x, y, mouseX, mouseY, delta);
                        } else {
                            page.renderRight(screen.getMinecraft(), guiGraphics, x + 90, y, mouseX, mouseY, delta);
                        }
                    }
                }
            }
        }

    }

    public void playSound() {
        Player playerEntity = Minecraft.getInstance().player;
        if (playerEntity != null) {
            playerEntity.playNotifySound(SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1.0f, 1.0f);
        }
    }

    public void nextPage() {
        if (grouping < openEntry.pages.size() / 2f - 1) {
            grouping += 1;
            playSound();
        }
    }

    public void previousPage() {
        if (grouping > 0) {
            grouping -= 1;
            playSound();
        }
    }
}
