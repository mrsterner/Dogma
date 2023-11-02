package dev.sterner.dogma.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    protected MobMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tickHeadTurn", at = @At("HEAD"), cancellable = true)
    public void book_of_the_dead$turnHead(float bodyRotation, float headRotation, CallbackInfoReturnable<Float> info) {
        if (this.deathTime > 0) {
            info.setReturnValue(0.0F);
        }
    }
}
