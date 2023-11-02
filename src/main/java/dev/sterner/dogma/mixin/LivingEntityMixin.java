package dev.sterner.dogma.mixin;

import dev.sterner.dogma.api.event.LivingDeathTickEvent;
import dev.sterner.dogma.foundation.capability.necro.NecroCorpseDataCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public int deathTime;

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tickDeath", at = @At("HEAD"), cancellable = true)
    protected void dogma$eventInject(CallbackInfo callbackInfo) {
        LivingDeathTickEvent livingDeathTickEvent = new LivingDeathTickEvent((LivingEntity)(Object) this);
        if(MinecraftForge.EVENT_BUS.post(livingDeathTickEvent)){
            callbackInfo.cancel();
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    private void dogma$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity)(Object)(this);
        NecroCorpseDataCapability capability = NecroCorpseDataCapability.getCapability(livingEntity);
        if (capability.isCorpse) {
            capability.shouldDie = true;
            if (!livingEntity.level().isClientSide) {
                NecroCorpseDataCapability.sync(livingEntity);
            }
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"), cancellable = true)
    private void book_of_the_dead$tickMovement(CallbackInfo callbackInfo) {
        LivingEntity livingEntity = (LivingEntity)(Object)(this);
        if (this.deathTime >= 20 && livingEntity instanceof Mob) {
            AABB box = this.getBoundingBox();
            if (this.level().containsAnyLiquid(box.move(0.0D, box.getYsize(), 0.0D))) {
                this.setPos(this.getX(), this.getY() + 0.05D, this.getZ());
            }
            callbackInfo.cancel();
        }
    }
}
