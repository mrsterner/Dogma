package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.content.enchantment.necro.ButcheringEnchantment;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface DogmaEnchantmentRegistry {

    DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Dogma.MODID);
    //start common

    //end common
    //------------------------------------------------
    //start abyss

    //end abyss
    //------------------------------------------------
    //start necro
    EnchantmentCategory BUTCHERING_AXE = EnchantmentCategory.create(Dogma.MODID + ":rebound_scythe_only", i -> i instanceof AxeItem);

    RegistryObject<Enchantment> BUTCHERING = ENCHANTMENTS.register("butchering", ButcheringEnchantment::new);

    //end necro
    //------------------------------------------------


}
