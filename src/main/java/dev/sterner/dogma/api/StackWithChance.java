package dev.sterner.dogma.api;

import net.minecraft.world.item.ItemStack;

public record StackWithChance(ItemStack itemStack, float chance) {
}
