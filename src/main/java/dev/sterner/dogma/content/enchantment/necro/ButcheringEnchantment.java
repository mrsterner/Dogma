package dev.sterner.dogma.content.enchantment.necro;

import dev.sterner.dogma.foundation.registry.DogmaEnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ButcheringEnchantment extends Enchantment {
    public ButcheringEnchantment() {
        super(Rarity.UNCOMMON, DogmaEnchantmentRegistry.BUTCHERING_AXE, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @Override
    public boolean isTreasureOnly() {
        return false;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinCost(int pLevel) {
        return pLevel * 25;
    }

    @Override
    public int getMaxCost(int pLevel) {
        return this.getMinCost(pLevel) + 50;
    }
}
