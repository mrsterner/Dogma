package dev.sterner.dogma.client.screen.necro.page;


import com.mojang.authlib.minecraft.client.MinecraftClient;
import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.util.ComponentUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class HeadlineBookPage extends BookPage {
    private final String headlineTranslationKey;
    private final String translationKey;

    protected HeadlineBookPage(String headlineTranslationKey, String translationKey) {
        super(Dogma.id("textures/gui/book/pages/headline_page.png"));
        this.headlineTranslationKey = headlineTranslationKey;
        this.translationKey = translationKey;
    }

    public static HeadlineBookPage of(String headlineTranslationKey, String translationKey) {
        return new HeadlineBookPage(headlineTranslationKey, translationKey);
    }

    public static HeadlineBookPage of(String headlineTranslationKey) {
        return new HeadlineBookPage(headlineTranslationKey, "empty");
    }


    public String headlineTranslationKey() {
        return "book_of_the_dead.gui.book.page.headline." + headlineTranslationKey;
    }

    public String translationKey() {
        return "book_of_the_dead.gui.book.page.text." + translationKey;
    }

    @Override
    public void renderLeft(Minecraft minecraft, GuiGraphics guiGraphics, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks) {
        int guiLeft = guiLeft();
        int guiTop = guiTop();
        Component component = Component.translatable(headlineTranslationKey());
        ComponentUtils.renderText(guiGraphics, component, guiLeft + 75 - 18 - minecraft.font.width(component.getString()) / 2, guiTop + 10);
        ComponentUtils.renderWrappingText(guiGraphics, translationKey(), guiLeft, guiTop + 31, 109);
    }

    @Override
    public void renderRight(Minecraft minecraft, GuiGraphics guiGraphics, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks) {
        int guiLeft = guiLeft();
        int guiTop = guiTop();
        Component component = Component.translatable(headlineTranslationKey());
        ComponentUtils.renderText(guiGraphics, component, guiLeft + 200 - 9 - minecraft.font.width(component.getString()) / 2, guiTop + 10);
        ComponentUtils.renderWrappingText(guiGraphics, translationKey(), guiLeft + 140, guiTop + 31, 109);
    }
}