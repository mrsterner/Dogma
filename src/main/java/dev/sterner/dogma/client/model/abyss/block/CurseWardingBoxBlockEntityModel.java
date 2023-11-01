package dev.sterner.dogma.client.model.abyss.block;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.content.block_entity.abyss.CurseWardingBoxBlockEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class CurseWardingBoxBlockEntityModel extends GeoModel<CurseWardingBoxBlockEntity> {

    private static final ResourceLocation MODEL = Dogma.id("geo/curse_warding_box_fix.geo.json");
    private static final ResourceLocation TEXTURE = Dogma.id("textures/block/curse_warding_box_outline_blue.png");
    private static final ResourceLocation ANIMATION = Dogma.id("animations/curse_warding_box.animation.json");

    @Override
    public ResourceLocation getModelResource(CurseWardingBoxBlockEntity curseWardingBoxBlockEntity) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(CurseWardingBoxBlockEntity curseWardingBoxBlockEntity) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(CurseWardingBoxBlockEntity curseWardingBoxBlockEntity) {
        return ANIMATION;
    }
}