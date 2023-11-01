package dev.sterner.dogma.foundation.abyss.abyss;

import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.abyss.curse.Curse;
import dev.sterner.dogma.foundation.registry.DogmaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class AbyssLayer {

    public int bottomY;
    public int topY;
    public Curse curse;

    public AbyssLayer(int topY, int bottomY, Curse curse) {
        this.topY = topY;
        this.bottomY = bottomY;
        this.curse = curse;
    }

    public static void writeAbyssLayerToNbt(AbyssLayer layerInfo, CompoundTag nbt) {
        nbt.putInt(Constants.Nbt.TOP_Y, layerInfo.topY);
        nbt.putInt(Constants.Nbt.BOTTOM_Y, layerInfo.bottomY);
        nbt.putString(Constants.Nbt.CURSE_INTENSITY, DogmaRegistries.CURSE_REGISTRY.get().getDelegateOrThrow(layerInfo.curse).get().toString());
    }

    public static void readAbyssLayerToNbt(AbyssLayer layerInfo, CompoundTag nbt) {
        layerInfo.bottomY = nbt.getInt(Constants.Nbt.BOTTOM_Y);
        layerInfo.topY = nbt.getInt(Constants.Nbt.TOP_Y);
        layerInfo.curse = DogmaRegistries.CURSE_REGISTRY.get().getDelegateOrThrow(new ResourceLocation(nbt.getString(Constants.Nbt.CURSE))).get();
    }
}