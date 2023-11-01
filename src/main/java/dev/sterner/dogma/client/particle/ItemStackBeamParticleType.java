package dev.sterner.dogma.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleType;

import javax.annotation.Nullable;

public class ItemStackBeamParticleType extends ParticleType<ItemStackBeamParticleOptions> {
    public ItemStackBeamParticleType() {
        super(false, ItemStackBeamParticleOptions.DESERIALIZER);
    }

    @Override
    public Codec<ItemStackBeamParticleOptions> codec() {
        return ItemStackBeamParticleOptions.brokenItemCodec(this);
    }

    public static class Factory implements ParticleProvider<ItemStackBeamParticleOptions> {

        public Factory() {
        }

        @Nullable
        @Override
        public Particle createParticle(ItemStackBeamParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new ItemStackBeamParticle(world, data, x, y, z, mx, my, mz);
        }
    }
}
