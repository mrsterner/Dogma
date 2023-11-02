package dev.sterner.dogma.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sterner.dogma.foundation.capability.necro.NecroCorpseDataCapability;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Inject(method = "renderShadow", at = @At(value = "HEAD"), cancellable = true)
    private static void dogma$renderShadow(PoseStack pPoseStack, MultiBufferSource pBuffer, Entity pEntity, float pWeight, float pPartialTicks, LevelReader pLevel, float pSize, CallbackInfo ci) {
        if (pEntity instanceof LivingEntity livingEntity) {
            NecroCorpseDataCapability capability = NecroCorpseDataCapability.getCapability(livingEntity);
            if (capability.isCorpse) {
                ci.cancel();
            }
        }
    }
}