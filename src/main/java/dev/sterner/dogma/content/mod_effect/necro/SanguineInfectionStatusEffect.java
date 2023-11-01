package dev.sterner.dogma.content.mod_effect.necro;

import dev.sterner.dogma.foundation.DogmaDamageSources;
import dev.sterner.dogma.foundation.registry.DogmaMobEffects;
import dev.sterner.dogma.foundation.registry.DogmaParticleTypeRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class SanguineInfectionStatusEffect extends MobEffect {
    public SanguineInfectionStatusEffect() {
        super(MobEffectCategory.NEUTRAL, 0xbd0000);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);
        pLivingEntity.hurt(DogmaDamageSources.create(pLivingEntity.level(), DogmaDamageSources.SANGUINE), 2.0F);
        pLivingEntity.level().playSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), SoundEvents.HONEY_BLOCK_BREAK, SoundSource.PLAYERS, 2, 1);
        var rand = pLivingEntity.getRandom();
        for (int i = 0; i < 24; ++i) {
            double x = pLivingEntity.getRandomX(0.5);
            double y = pLivingEntity.getY() + rand.nextDouble() * 2;
            double z = pLivingEntity.getRandomZ(0.5);

            double dx = (rand.nextFloat() / 2.0F);
            double dy = 5.0E-5;
            double dz = (rand.nextFloat() / 2.0F);
            pLivingEntity.level().addParticle(DogmaParticleTypeRegistry.SPLASHING_BLOOD.get(), x, y, z, dx, dy, dz);
        }

        for (MobEffectInstance statusEffectInstance : pLivingEntity.getActiveEffects()) {
            if (statusEffectInstance.getEffect() == MobEffects.POISON || statusEffectInstance.getEffect() == MobEffects.WITHER || statusEffectInstance.getEffect() == DogmaMobEffects.SANGUINE.get()) {
                if (statusEffectInstance.getDuration() > 20 * 2) {
                    statusEffectInstance.duration = statusEffectInstance.getDuration() / 2;
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int i = 40 >> pAmplifier;
        if (i > 0) {
            return pDuration % i == 0;
        } else {
            return true;
        }
    }
}