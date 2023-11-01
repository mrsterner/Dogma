package dev.sterner.dogma.foundation.abyss.curse;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class Curse {
    public CurseIntensity intensity;

    public Curse(CurseIntensity curseIntensity) {
        this.intensity = curseIntensity;
    }

    public CurseIntensity getIntensity() {
        return intensity;
    }

    public void tickEffect(Level level, LivingEntity livingEntity) {

    }

    public void tickClientEffect(ClientLevel clientLevel, LivingEntity livingEntity) {

    }

    public void tickAscensionEffectClient(ClientLevel clientLevel, LivingEntity livingEntity) {

    }

    public void tickAscensionEffect(Level level, LivingEntity livingEntity) {

    }
}