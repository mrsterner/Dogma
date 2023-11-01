package dev.sterner.dogma.client.renderer.abyss.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.sterner.dogma.client.model.abyss.block.CurseWardingBoxBlockEntityModel;
import dev.sterner.dogma.content.block_entity.abyss.CurseWardingBoxBlockEntity;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.renderer.GeoBlockRenderer;
import mod.azure.azurelib.renderer.layer.BlockAndItemGeoLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;


public class CurseWardingBoxBlockEntityRenderer extends GeoBlockRenderer<CurseWardingBoxBlockEntity> {
    public CurseWardingBoxBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        super(new CurseWardingBoxBlockEntityModel());
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, CurseWardingBoxBlockEntity animatable) {
                if (bone.getName().equals("item") && animatable.item != null) {
                    return animatable.item.getDefaultInstance();
                } else {
                    return null;
                }
            }

            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, CurseWardingBoxBlockEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                poseStack.pushPose();
                poseStack.scale(0.5f, 0.5f, 0.5f);
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
                poseStack.popPose();
            }
        });
    }

    @Override
    protected void rotateBlock(Direction facing, PoseStack poseStack) {
        switch (facing) {
            case SOUTH -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                poseStack.translate(-1, 0, -1);
            }
            case WEST -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                poseStack.translate(-1, 0, 0);
            }
            case NORTH -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
                poseStack.translate(0, 0, 0);
            }
            case EAST -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
                poseStack.translate(0, 0, -1);
            }
            default -> {
            }
        }

    }
}
