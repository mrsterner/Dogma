package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public interface DogmaSoundEventRegistry {
    DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Dogma.MODID);
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
