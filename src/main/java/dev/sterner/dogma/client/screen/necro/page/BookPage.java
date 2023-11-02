package dev.sterner.dogma.client.screen.necro.page;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import dev.sterner.dogma.client.screen.necro.BookOfTheDeadScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class BookPage {
    public final ResourceLocation BACKGROUND;

    public BookPage(ResourceLocation background) {
        this.BACKGROUND = background;
    }

    public boolean isValid() {
        return true;
    }

    public void renderLeft(Minecraft minecraft, GuiGraphics guiGraphics, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks) {

    }

    public void renderRight(Minecraft minecraft, GuiGraphics guiGraphics, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks) {

    }

    public int guiLeft() {
        return (BookOfTheDeadScreen.screen.width - 192) / 4 - 3;
    }

    public int guiTop() {
        return 18 * 2;
    }
}