package dev.sterner.dogma.content.mod_effect.necro;

import dev.sterner.dogma.foundation.capability.necro.NecroLivingEntityDataCapability;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class MorphineStatusEffect extends MobEffect {
    public MorphineStatusEffect() {
        super(MobEffectCategory.NEUTRAL, 0xffffff);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        NecroLivingEntityDataCapability capability = NecroLivingEntityDataCapability.getCapability(pLivingEntity);
        pLivingEntity.hurt(pLivingEntity.level().damageSources().generic(), capability.getMorphine$accumulatedDamage());
        capability.setMorphine$accumulatedDamage(0.0F);

        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }
}
