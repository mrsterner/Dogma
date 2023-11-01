package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.abyss.curse.Curse;
import dev.sterner.dogma.foundation.abyss.curse.CurseIntensity;
import dev.sterner.dogma.foundation.abyss.curse.curses.MediumCurse;
import dev.sterner.dogma.foundation.abyss.curse.curses.SevereCurse;
import dev.sterner.dogma.foundation.abyss.curse.curses.StrongCurse;
import dev.sterner.dogma.foundation.abyss.curse.curses.WeakCurse;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public interface DogmaRegistries {
    //Curse key
    ResourceKey<Registry<Curse>> CURSE = ResourceKey.createRegistryKey(Dogma.id("curse"));

    //Curse Registry
    DeferredRegister<Curse> CURSE_DEFERRED_REGISTER = DeferredRegister.create(DogmaRegistries.CURSE, Dogma.MODID);

    //
    Supplier<IForgeRegistry<Curse>> CURSE_REGISTRY = DogmaRegistries.makeSyncedRegistry(CURSE_DEFERRED_REGISTER);

    //Curses
    //TODO change new Curse(...) to implementation
    RegistryObject<Curse> NONE = DogmaRegistries.CURSE_DEFERRED_REGISTER.register("none", () -> new Curse(CurseIntensity.NONE));
    RegistryObject<Curse> WEAK = DogmaRegistries.CURSE_DEFERRED_REGISTER.register("weak", () -> new WeakCurse(CurseIntensity.WEAK));
    RegistryObject<Curse> MEDIUM = DogmaRegistries.CURSE_DEFERRED_REGISTER.register("medium", () -> new MediumCurse(CurseIntensity.MEDIUM));
    RegistryObject<Curse> STRONG = DogmaRegistries.CURSE_DEFERRED_REGISTER.register("strong", () -> new StrongCurse(CurseIntensity.STRONG));
    RegistryObject<Curse> SEVERE = DogmaRegistries.CURSE_DEFERRED_REGISTER.register("severe", () -> new SevereCurse(CurseIntensity.SEVERE));

    private static <T> Supplier<IForgeRegistry<T>> makeSyncedRegistry(DeferredRegister<T> deferredRegister) {
        return deferredRegister.makeRegistry(() -> new RegistryBuilder<T>().disableSaving());
    }
}
