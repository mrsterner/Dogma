package dev.sterner.dogma.client.renderer.abyss.gui.hud;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.capability.abyss.AbyssLivingEntityDataCapability;
import dev.sterner.dogma.foundation.handler.abyss.CurseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class CurseHudRenderer {

    public static void renderCurseHud(ForgeGui gui, GuiGraphics guiGraphics, int width, int height) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
            gui.setupOverlayRenderState(true, false);
            LocalPlayer player = minecraft.player;
            if (!player.isCreative() && !player.isSpectator()) {
                CurseHandler curseManager = AbyssLivingEntityDataCapability.getCapability(player).curseHandler;

                if (1 > 0) {
                    float absorb = Mth.ceil(player.getAbsorptionAmount());
                    float maxHealth = (float) player.getAttribute(Attributes.MAX_HEALTH).getValue();
                    float armor = (float) player.getAttribute(Attributes.ARMOR).getValue();

                    int left = width / 2 - 91;
                    int top = height - gui.leftHeight;

                    if (armor != 0) {
                        top -= 10;
                    }

                    int healthRows = Mth.ceil((maxHealth + absorb) / 2.0F / 10.0F);
                    int rowHeight = Math.max(10 - (healthRows - 2), 3);

                    int size = 13;
                    float skull = 5f;//TODO
                    for (int i = 0; i < Math.ceil(Math.floor(skull) / 3f); i++) {
                        int row = (int) ((double) i / 10f);
                        int x = left + i % 10 * 8;
                        int y = top - row * 4 + rowHeight * 2 - 13;
                        int progress = Math.min(3, (int) skull - i * 3);
                        int xTextureOffset = 1 + (3 - progress) * 15;

                        guiGraphics.blit(getCurseTexture(), x - 2, y - 2, size, size, xTextureOffset, 0, 13, 13, 45, 45);
                    }
                }
            }
        }
    }

    private static ResourceLocation getCurseTexture() {
        return Dogma.id("textures/gui/curse.png");
    }
}