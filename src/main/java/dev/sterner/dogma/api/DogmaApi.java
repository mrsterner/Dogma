package dev.sterner.dogma.api;

import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.registry.DogmaEnchantmentRegistry;
import dev.sterner.dogma.foundation.registry.DogmaItemRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import javax.annotation.Nullable;

public class DogmaApi {
    /**
     * Check if a killed entity should become a corpse or not with the EntityTag, Enchantment and ItemStack
     *
     * @param livingEntity killed entity
     * @return true if killed entity should become a corpse
     */
    public static boolean isButchering(@Nullable LivingEntity livingEntity) {
        if (livingEntity == null) {
            return false;
        } else if (livingEntity.getLastHurtByMob() instanceof Player player) {
            boolean isInTag = livingEntity.getType().is(Constants.Tags.BUTCHERABLE);
            boolean isItem = (player.getMainHandItem().is(DogmaItemRegistry.MEAT_CLEAVER.get()) || player.getMainHandItem().getEnchantmentLevel(DogmaEnchantmentRegistry.BUTCHERING.get()) != 0);
            return isInTag && isItem;
        }
        return false;
    }
}
