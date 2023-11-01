package dev.sterner.dogma.api;

import net.minecraft.server.dedicated.Settings;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;

public class MobEffectItem extends Item {
    private final MobEffect effect;

    public MobEffectItem(Properties properties, MobEffect effect) {
        super(properties);
        this.effect = effect;
    }

    public MobEffect getStatusEffect() {
        return effect;
    }
}
