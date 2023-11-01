package dev.sterner.dogma.client.particle;

import dev.sterner.dogma.foundation.registry.DogmaParticleTypeRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.lodestar.lodestone.systems.particle.options.WorldParticleOptions;
import team.lodestar.lodestone.systems.particle.type.LodestoneParticleType;
import team.lodestar.lodestone.systems.particle.world.GenericParticle;

public class BloodDripParticle extends DripParticle {
    private final Fluid fluid;
    protected boolean isGlowing;

    public BloodDripParticle(ClientLevel world, double x, double y, double z, Fluid fluid) {
        super(world, x, y, z, fluid);
        this.setSize(0.01F, 0.01F);
        this.gravity = 0.06F;
        this.fluid = fluid;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float pPartialTick) {
        return this.isGlowing ? 240 : super.getLightColor(pPartialTick);
    }



    protected void updateAge() {
        if (this.lifetime-- <= 0) {
            this.remove();
        }
    }

    protected void updateVelocity() {
    }

    public static TextureSheetParticle createBloodHangParticle(
            SimpleParticleType defaultParticleType, ClientLevel world, double d, double e, double f, double g, double h, double i
    ) {
        BloodDripParticle.Dripping dripping = new BloodDripParticle.Dripping(world, d, e, f, Fluids.EMPTY, DogmaParticleTypeRegistry.FALLING_BLOOD.get());
        dripping.gravity *= 0.01F;
        dripping.lifetime = 100;
        dripping.setColor(0.62F, 0.0F, 0.1F);
        return dripping;
    }

    public static TextureSheetParticle createBloodFallParticle(
            SimpleParticleType defaultParticleType, ClientLevel world, double d, double e, double f, double g, double h, double i
    ) {
        BloodDripParticle blockLeakParticle = new BloodDripParticle.FallingBlood(world, d, e, f, Fluids.EMPTY, DogmaParticleTypeRegistry.LANDING_BLOOD.get());
        blockLeakParticle.gravity = 0.01F;
        blockLeakParticle.setColor(0.67F, 0.04F, 0.05F);
        return blockLeakParticle;
    }

    public static TextureSheetParticle createBloodLandParticle(
            SimpleParticleType defaultParticleType, ClientLevel world, double d, double e, double f, double g, double h, double i
    ) {
        BloodDripParticle blockLeakParticle = new BloodDripParticle.Landing(world, d, e, f, Fluids.EMPTY);
        blockLeakParticle.lifetime = (int) (128.0 / (Math.random() * 0.8 + 0.2));
        blockLeakParticle.setColor(0.67F, 0.04F, 0.05F);
        return blockLeakParticle;
    }

    static class Dripping extends BloodDripParticle {
        private final ParticleOptions nextParticle;

        Dripping(ClientLevel world, double x, double y, double z, Fluid fluid, ParticleOptions nextParticle) {
            super(world, x, y, z, fluid);
            this.nextParticle = nextParticle;
            this.gravity *= 0.02F;
            this.lifetime = 40;
        }

        @Override
        protected void updateAge() {
            if (this.lifetime-- <= 0) {
                this.remove();
                this.level.addParticle(this.nextParticle, this.x, this.y, this.z, this.xd, this.yd, this.zd);
            }
        }

        @Override
        protected void updateVelocity() {
            this.xd *= 0.02;
            this.yd *= 0.02;
            this.zd *= 0.02;
        }
    }

    static class Falling extends BloodDripParticle {
        Falling(ClientLevel clientWorld, double d, double e, double f, Fluid fluid) {
            this(clientWorld, d, e, f, fluid, (int) (64.0 / (Math.random() * 0.8 + 0.2)));
        }

        Falling(ClientLevel world, double x, double y, double z, Fluid fluid, int maxAge) {
            super(world, x, y, z, fluid);
            this.lifetime = maxAge;
        }

        @Override
        protected void updateVelocity() {
            if (this.onGround) {
                this.remove();
            }
        }
    }

    public static class ContinuousFalling extends BloodDripParticle.Falling {
        protected final ParticleOptions nextParticle;

        ContinuousFalling(ClientLevel world, double x, double y, double z, Fluid fluid, ParticleOptions nextParticle) {
            super(world, x, y, z, fluid);
            this.nextParticle = nextParticle;
        }

        @Override
        protected void updateVelocity() {
            if (this.onGround) {
                this.remove();
                this.level.addParticle(this.nextParticle, this.x, this.y, this.z, 0.0, 0.0, 0.0);
            }
        }
    }

    static class FallingBlood extends BloodDripParticle.ContinuousFalling {
        FallingBlood(ClientLevel clientWorld, double d, double e, double f, Fluid fluid, ParticleOptions particleEffect) {
            super(clientWorld, d, e, f, fluid, particleEffect);
        }

        @Override
        protected void updateVelocity() {
            if (this.onGround) {
                this.remove();
                this.level.addParticle(this.nextParticle, this.x, this.y, this.z, 0.0, 0.0, 0.0);
                float f = Mth.nextFloat(this.random, 0.3F, 1.0F);
                this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.BEEHIVE_DRIP, SoundSource.BLOCKS, f, 1.0F, false);
            }
        }
    }

    static class Landing extends BloodDripParticle {
        Landing(ClientLevel clientWorld, double d, double e, double f, Fluid fluid) {
            super(clientWorld, d, e, f, fluid);
            this.lifetime = (int) (16.0 / (Math.random() * 0.8 + 0.2));
        }
    }
}