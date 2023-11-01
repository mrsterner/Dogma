package dev.sterner.dogma.content.mod_effect.necro;

public class SanguineInfectionStatusEffect extends StatusEffect {
    public SanguineInfectionStatusEffect(StatusEffectType type) {
        super(type, 0xbd0000);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        entity.damage(BotDDamageTypes.getDamageSource(entity.getWorld(), BotDDamageTypes.SANGUINE), 2.0F);
        entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundCategory.PLAYERS, 2, 1);
        var rand = entity.getRandom();
        for (int i = 0; i < 24; ++i) {
            double x = entity.getParticleX(0.5);
            double y = entity.getY() + rand.nextDouble() * 2;
            double z = entity.getParticleZ(0.5);

            double dx = (rand.nextFloat() / 2.0F);
            double dy = 5.0E-5;
            double dz = (rand.nextFloat() / 2.0F);
            entity.world.addParticle(BotDParticleTypes.SPLASHING_BLOOD, x, y, z, dx, dy, dz);
        }

        for (StatusEffectInstance statusEffectInstance : entity.getStatusEffects()) {
            if (statusEffectInstance.getEffectType() == StatusEffects.POISON || statusEffectInstance.getEffectType() == StatusEffects.WITHER || statusEffectInstance.getEffectType() == BotDStatusEffects.SANGUINE) {
                if (statusEffectInstance.getDuration() > 20 * 2) {
                    statusEffectInstance.duration = statusEffectInstance.getDuration() / 2;
                }
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 40 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}