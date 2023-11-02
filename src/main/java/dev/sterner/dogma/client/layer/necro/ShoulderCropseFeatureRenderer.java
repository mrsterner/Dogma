package dev.sterner.dogma.client.layer.necro;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.client.model.necro.entity.BagEntityModel;
import dev.sterner.dogma.foundation.capability.necro.NecroPlayerDataCapability;
import dev.sterner.dogma.foundation.capability.necro.NecroPlayerHaulerDataCapability;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

public class ShoulderCropseFeatureRenderer<T extends Player> extends RenderLayer<T, PlayerModel<T>> {
    private final EntityRenderDispatcher dispatcher;
    private final BagEntityModel model;
    public static final ResourceLocation BAG = Dogma.id("textures/entity/bag.png");

    public ShoulderCropseFeatureRenderer(RenderLayerParent<T, PlayerModel<T>> pRenderer, EntityModelSet pModelSet, EntityRenderDispatcher dispatcher) {
        super(pRenderer);
        this.dispatcher = dispatcher;
        this.model = new BagEntityModel(pModelSet.bakeLayer(BagEntityModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T player, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        pPoseStack.pushPose();

        NecroPlayerHaulerDataCapability capability = NecroPlayerHaulerDataCapability.getCapability(player);
        CompoundTag nbt = capability.getCorpseData();
        EntityType.create(nbt, player.level()).ifPresent(type -> {
            if (type instanceof LivingEntity livingEntity && dispatcher != null) {
                livingEntity.hurtTime = 0;
                livingEntity.deathTime = 0;
                dispatcher.setRenderShadow(false);
                pPoseStack.pushPose();
                pPoseStack.mulPose(Axis.XP.rotationDegrees(player.isCrouching() ? 20 : 0));
                if (livingEntity instanceof Animal) {
                    renderQuadraped(pPoseStack, pBuffer, pPackedLight, livingEntity);
                } else {
                    renderHumanoid(pPoseStack, pBuffer, pPackedLight, livingEntity);
                }
                pPoseStack.popPose();
            }
        });

        pPoseStack.popPose();
    }

    private void renderHumanoid(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, LivingEntity livingEntity) {
        pPoseStack.pushPose();
        if (livingEntity.isBaby()) {
            pPoseStack.translate(0, -0.25, 0.5);
        } else {
            float f = 0.75f;
            pPoseStack.scale(f, f, f);
            pPoseStack.translate(0, -1.0, 0.5);
        }
        dispatcher.render(livingEntity, 0, 0, 0, 0, 0, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.popPose();

        pPoseStack.pushPose();
        pPoseStack.translate(0, -0.5, 0.6);
        float g = 1.1f;
        pPoseStack.scale(g, g, g);
        this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(BAG)), pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        pPoseStack.popPose();
    }

    private void renderQuadraped(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, LivingEntity livingEntity) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.XN.rotationDegrees(90));
        if (livingEntity.isBaby()) {
            pPoseStack.translate(0, -1, 0);
        } else {
            float f = 0.75f;
            pPoseStack.scale(f, f, f);
            pPoseStack.translate(0, -1.5, 0);
        }
        dispatcher.render(livingEntity, 0, 0, 0, 0, 0, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.popPose();

        pPoseStack.pushPose();
        pPoseStack.translate(0, -0.5, 0.6);
        float g = 1.1f;
        pPoseStack.scale(g, g, g);
        this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(BAG)), pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        pPoseStack.popPose();
    }
}
