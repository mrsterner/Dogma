package dev.sterner.dogma.client.screen.necro.page;


import com.mojang.authlib.minecraft.client.MinecraftClient;
import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.util.ComponentUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class TextPage extends BookPage {
    public final String translationKey;

    protected TextPage(String translationKey) {
        super(Dogma.id("textures/gui/book/pages/blank_page.png"));
        this.translationKey = translationKey;
    }

    public static TextPage of(String translationKey) {
        return new TextPage(translationKey);
    }

    public static TextPage of() {
        return new TextPage("empty");
    }

    public String translationKey() {
        return "book_of_the_dead.gui.book.page.text." + translationKey;
    }

    @Override
    public void renderLeft(Minecraft minecraft, GuiGraphics guiGraphics, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks) {
        int guiLeft = guiLeft();
        int guiTop = guiTop();
        ComponentUtils.renderWrappingText(guiGraphics, translationKey(), guiLeft, guiTop + 10, 109);
    }

    @Override
    public void renderRight(Minecraft minecraft, GuiGraphics guiGraphics, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks) {
        int guiLeft = guiLeft();
        int guiTop = guiTop();
        ComponentUtils.renderWrappingText(guiGraphics, translationKey(), guiLeft + 140, guiTop + 10, 109);
    }
}