package dev.sterner.dogma.content.mod_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EmptyMobEffect extends MobEffect {
    public EmptyMobEffect() {
        super(MobEffectCategory.NEUTRAL, 0xffffff);
    }
}
