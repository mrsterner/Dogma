package dev.sterner.dogma.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public record PedestalInfo(NonNullList<ItemStack> stacks, BlockPos pos) {

}