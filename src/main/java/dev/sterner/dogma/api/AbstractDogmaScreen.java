package dev.sterner.dogma.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public abstract class AbstractDogmaScreen extends Screen {
    protected AbstractDogmaScreen(Component pTitle) {
        super(pTitle);
    }

    public void playPageFlipSound(Supplier<SoundEvent> soundEvent, float pitch) {
        playSound(soundEvent, Math.max(1, pitch * 0.8f));
    }

    public void playSound(Supplier<SoundEvent> soundEvent) {
        playSound(soundEvent, 1);
    }

    public void playSound(Supplier<SoundEvent> soundEvent, float pitch) {
        Player playerEntity = Minecraft.getInstance().player;
        playerEntity.playNotifySound(soundEvent.get(), SoundSource.PLAYERS, 1f, pitch);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
