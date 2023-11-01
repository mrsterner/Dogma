package dev.sterner.dogma.content.mod_effect.necro;

import dev.sterner.dogma.client.particle.OrbitParticleEffect;
import dev.sterner.dogma.foundation.DogmaDamageSources;
import dev.sterner.dogma.foundation.capability.necro.NecroLivingEntityDataCapability;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class SoulSiphonStatusEffect extends MobEffect {
    public SoulSiphonStatusEffect() {
        super(MobEffectCategory.NEUTRAL, 0x91db69);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        pLivingEntity.hurt(DogmaDamageSources.create(pLivingEntity.level(), DogmaDamageSources.SACRIFICE), Integer.MAX_VALUE);
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int i = 25 >> pAmplifier;
        if (i > 0) {
            return pDuration % i == 0;
        } else {
            return true;
        }
    }



    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);
        generateFX(pLivingEntity.level(), pLivingEntity);
    }

    public void generateFX(Level world, LivingEntity living) {
        NecroLivingEntityDataCapability capability = NecroLivingEntityDataCapability.getCapability(living);
        if (capability.getRitualPos() != null) {

            Vec3 b = capability.getRitualPos().subtract(new Vec3(living.getX(), living.getY(), living.getZ()).add(0.5, 1.5, 0.5));
            Vec3 directionVector = new Vec3(b.x(), b.y(), b.z());

            double x = living.getRandomX(0.25D);
            double y = living.getRandomY();
            double z = living.getRandomZ(0.25D);

            if (world instanceof ServerLevel serverWorld) {
                serverWorld.sendParticles(new OrbitParticleEffect(1, 0, 0.25f,//TODO lodestone particle
                                (float) capability.getRitualPos().x(),
                                (float) capability.getRitualPos().y() + 1,
                                (float) capability.getRitualPos().z(), 3),
                        x,
                        y,
                        z,
                        0,
                        0,
                        0,
                        0,
                        0);
            }

            for (int i = 0; i < 4; i++) {
                if (world instanceof ServerLevel serverWorld) {
                    //serverWorld.spawnParticles(new SoulParticleEffect(1, 0, 0.25f), x, y, z, 0, directionVector.x, directionVector.y, directionVector.z, 0.05);
                }
            }

        }
    }
}