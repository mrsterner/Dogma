package dev.sterner.dogma.content.mod_effect.necro;

import dev.sterner.dogma.foundation.DogmaDamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class EuthanasiaStatusEffect extends MobEffect {
    public EuthanasiaStatusEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        entity.hurt(DogmaDamageSources.create(entity.level(), DamageTypes.FELL_OUT_OF_WORLD), Integer.MAX_VALUE);
        super.removeAttributeModifiers(entity, attributes, amplifier);
    }
}
