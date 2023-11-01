package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public interface DogmaMenuTypeRegistry {
    DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Dogma.MODID);
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
