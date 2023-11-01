package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public interface DogmaAttributeRegistry {
    DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Dogma.MODID);
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
