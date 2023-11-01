package dev.sterner.dogma.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;

public class BloodSplashParticle extends TextureSheetParticle {
    private final float sampleU;
    private final float sampleV;
    private Entity targetEntity;

    public BloodSplashParticle(ClientLevel pLevel, double pX, double pY, double pZ, double dx, double dy, double dz) {
        super(pLevel, pX, pY, pZ);
        this.gravity = 0.75F;
        this.friction = 0.999F;
        this.xd *= 0.8F;
        this.yd *= 0.8F;
        this.zd *= 0.8F;
        this.yd = (this.random.nextFloat() * 0.2F + 0.05F);
        this.quadSize /= 2;
        this.quadSize *= this.random.nextFloat() * 2.0F + 0.2F;
        this.lifetime = (int) (64.0 / (Math.random() * 0.8 + 0.2));

        this.sampleU = this.random.nextFloat() * 3.0F;
        this.sampleV = this.random.nextFloat() * 3.0F;
    }

    @Override
    public void tick() {
        super.tick();
        if (targetEntity != null) {
            attachToEntity(targetEntity);
        }
    }

    private void attachToEntity(Entity targetEntity) {
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected float getU0() {
        return this.sprite.getU((this.sampleU + 1.0F) / 4.0F * 16.0F);
    }

    @Override
    protected float getU1() {
        return this.sprite.getU(this.sampleU / 4.0F * 16.0F);
    }

    @Override
    protected float getV0() {
        return this.sprite.getV(this.sampleV / 4.0F * 16.0F);
    }

    @Override
    protected float getV1() {
        return this.sprite.getV((this.sampleV + 1.0F) / 4.0F * 16.0F);
    }
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double x, double y, double z, double dx, double dy, double dz) {
            BloodSplashParticle bloodSplashParticle = new BloodSplashParticle(clientWorld, x, y, z, dx, dy + 1.0, dz);
            bloodSplashParticle.pickSprite(this.spriteProvider);
            return bloodSplashParticle;
        }
    }
}
