package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public interface DogmaEnchantmentRegistry {

    DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Dogma.MODID);
    //start common

    //end common
    //------------------------------------------------
    //start abyss

    //end abyss
    //------------------------------------------------
    //start necro

    //end necro
    //------------------------------------------------


}
