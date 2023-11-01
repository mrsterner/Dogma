package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.content.mod_effect.EmptyMobEffect;
import dev.sterner.dogma.content.mod_effect.necro.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface DogmaMobEffects {
    DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Dogma.MODID);
    //start common

    //end common
    //------------------------------------------------
    //start abyss

    //end abyss
    //------------------------------------------------
    //start necro
    RegistryObject<MobEffect> EUTHANASIA = EFFECTS.register("euthanasia", EuthanasiaStatusEffect::new);
    RegistryObject<MobEffect> ADRENALINE = EFFECTS.register("adrenaline", EmptyMobEffect::new);
    RegistryObject<MobEffect> MORPHINE = EFFECTS.register("morphine", MorphineStatusEffect::new);
    RegistryObject<MobEffect> SANGUINE = EFFECTS.register("sanguine_infection", SanguineInfectionStatusEffect::new);
    RegistryObject<MobEffect> SOUL_SIPHON = EFFECTS.register("soul_siphon", SoulSiphonStatusEffect::new);
    RegistryObject<MobEffect> SOUL_SICKNESS = EFFECTS.register("soul_sickness", SoulSicknessStatusEffect::new);
    //end necro
    //------------------------------------------------
}
