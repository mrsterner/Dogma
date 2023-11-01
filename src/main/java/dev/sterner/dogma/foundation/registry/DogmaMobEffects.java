package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public interface DogmaMobEffects {
    DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Dogma.MODID);
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
