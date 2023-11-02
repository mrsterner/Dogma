package dev.sterner.dogma.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public record PedestalInfo(ItemStack stack, BlockPos pos) {

}