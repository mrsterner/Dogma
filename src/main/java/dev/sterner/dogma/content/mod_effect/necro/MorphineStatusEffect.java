package dev.sterner.dogma.content.mod_effect.necro;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class MorphineStatusEffect extends MobEffect {
    public MorphineStatusEffect() {
        super(MobEffectCategory.NEUTRAL, 0xffffff);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        LivingEntityDataComponent component = BotDComponents.LIVING_COMPONENT.get(entity);
        entity.damage(entity.getDamageSources().generic(), component.getMorphine$accumulatedDamage());
        component.setMorphine$accumulatedDamage(0.0F);

        super.onRemoved(entity, attributes, amplifier);
    }
}
