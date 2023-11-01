package dev.sterner.dogma.content.mod_effect.necro;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class SoulSiphonStatusEffect extends MobEffect {
    public SoulSiphonStatusEffect() {
        super(MobEffectCategory.NEUTRAL, 0x91db69);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.damage(BotDDamageTypes.getDamageSource(entity.world, BotDDamageTypes.SACRIFICE), Integer.MAX_VALUE);
        super.onRemoved(entity, attributes, amplifier);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        generateFX(entity.world, entity);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 25 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }

    public void generateFX(World world, LivingEntity living) {
        LivingEntityDataComponent component = BotDComponents.LIVING_COMPONENT.get(living);
        if (component.getRitualPos() != null) {

            Vec3d b = component.getRitualPos().subtract(new Vec3d(living.getX(), living.getY(), living.getZ()).add(0.5, 1.5, 0.5));
            Vec3d directionVector = new Vec3d(b.getX(), b.getY(), b.getZ());

            double x = living.getParticleX(0.25D);
            double y = living.getRandomBodyY();
            double z = living.getParticleZ(0.25D);

            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(new OrbitParticleEffect(1, 0, 0.25f,
                                (float) component.getRitualPos().getX(),
                                (float) component.getRitualPos().getY() + 1,
                                (float) component.getRitualPos().getZ(), 3),
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
                if (world instanceof ServerWorld serverWorld) {
                    //serverWorld.spawnParticles(new SoulParticleEffect(1, 0, 0.25f), x, y, z, 0, directionVector.x, directionVector.y, directionVector.z, 0.05);
                }
            }

        }
    }
}