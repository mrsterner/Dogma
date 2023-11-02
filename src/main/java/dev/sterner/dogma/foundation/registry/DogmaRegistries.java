package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.api.knowledge.Knowledge;
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

import java.util.List;
import java.util.function.Supplier;

public interface DogmaRegistries {
    //Curse key
    ResourceKey<Registry<Curse>> CURSE = ResourceKey.createRegistryKey(Dogma.id("curse"));
    ResourceKey<Registry<Knowledge>> KNOWLEDGE = ResourceKey.createRegistryKey(Dogma.id("knowledge"));

    //Curse Registry
    DeferredRegister<Curse> CURSE_DEFERRED_REGISTER = DeferredRegister.create(DogmaRegistries.CURSE, Dogma.MODID);
    DeferredRegister<Knowledge> KNOWLEDGE_DEFERRED_REGISTER = DeferredRegister.create(DogmaRegistries.KNOWLEDGE, Dogma.MODID);

    //
    Supplier<IForgeRegistry<Curse>> CURSE_REGISTRY = DogmaRegistries.makeSyncedRegistry(CURSE_DEFERRED_REGISTER);
    Supplier<IForgeRegistry<Knowledge>> KNOWLEDGE_REGISTRY = DogmaRegistries.makeSyncedRegistry(KNOWLEDGE_DEFERRED_REGISTER);

    //Curses
    //TODO change new Curse(...) to implementation
    RegistryObject<Curse> NONE = DogmaRegistries.CURSE_DEFERRED_REGISTER.register("none", () -> new Curse(CurseIntensity.NONE));
    RegistryObject<Curse> WEAK = DogmaRegistries.CURSE_DEFERRED_REGISTER.register("weak", () -> new WeakCurse(CurseIntensity.WEAK));
    RegistryObject<Curse> MEDIUM = DogmaRegistries.CURSE_DEFERRED_REGISTER.register("medium", () -> new MediumCurse(CurseIntensity.MEDIUM));
    RegistryObject<Curse> STRONG = DogmaRegistries.CURSE_DEFERRED_REGISTER.register("strong", () -> new StrongCurse(CurseIntensity.STRONG));
    RegistryObject<Curse> SEVERE = DogmaRegistries.CURSE_DEFERRED_REGISTER.register("severe", () -> new SevereCurse(CurseIntensity.SEVERE));

    //Knowledge
    RegistryObject<Knowledge> ALCHEMY = KNOWLEDGE_DEFERRED_REGISTER.register("alchemy", () -> new Knowledge("alchemy", Dogma.id("textures/gui/knowledge/alchemy.png"), List.of()));

    RegistryObject<Knowledge> CALCINATION = KNOWLEDGE_DEFERRED_REGISTER.register("calcination", () -> new Knowledge("calcination", Dogma.id("textures/gui/knowledge/calcination.png"), List.of(ALCHEMY.get())));
    RegistryObject<Knowledge> DISSOLUTION = KNOWLEDGE_DEFERRED_REGISTER.register("dissolution", () -> new Knowledge("dissolution", Dogma.id("textures/gui/knowledge/dissolution.png"), List.of(ALCHEMY.get())));
    RegistryObject<Knowledge> SEPARATION = KNOWLEDGE_DEFERRED_REGISTER.register("separation", () -> new Knowledge("separation", Dogma.id("textures/gui/knowledge/separation.png"), List.of(DISSOLUTION.get(), CALCINATION.get())));
    RegistryObject<Knowledge> CONJUNCTION = KNOWLEDGE_DEFERRED_REGISTER.register("conjunction", () -> new Knowledge("conjunction", Dogma.id("textures/gui/knowledge/conjunction.png"), List.of(SEPARATION.get())));
    RegistryObject<Knowledge> FERMENTATION = KNOWLEDGE_DEFERRED_REGISTER.register("fermentation", () -> new Knowledge("fermentation", Dogma.id("textures/gui/knowledge/fermentation.png"), List.of(SEPARATION.get())));
    RegistryObject<Knowledge> DISTILLATION = KNOWLEDGE_DEFERRED_REGISTER.register("distillation", () -> new Knowledge("distillation", Dogma.id("textures/gui/knowledge/distillation.png"), List.of(CONJUNCTION.get(), FERMENTATION.get())));
    RegistryObject<Knowledge> COAGULATION = KNOWLEDGE_DEFERRED_REGISTER.register("coagulation", () -> new Knowledge("coagulation", Dogma.id("textures/gui/knowledge/coagulation.png"), List.of(DISTILLATION.get())));
    RegistryObject<Knowledge> PHILOSOPHER = KNOWLEDGE_DEFERRED_REGISTER.register("philosopher", () -> new Knowledge("philosopher", Dogma.id("textures/gui/knowledge/philosopher.png"), List.of(COAGULATION.get())));

    RegistryObject<Knowledge> BRIMSTONE = KNOWLEDGE_DEFERRED_REGISTER.register("brimstone", () -> new Knowledge("brimstone", Dogma.id("textures/gui/knowledge/brimstone.png"), List.of(ALCHEMY.get())));
    RegistryObject<Knowledge> ASH = KNOWLEDGE_DEFERRED_REGISTER.register("ash", () -> new Knowledge("ash", Dogma.id("textures/gui/knowledge/ash.png"), List.of(BRIMSTONE.get())));
    RegistryObject<Knowledge> MELT = KNOWLEDGE_DEFERRED_REGISTER.register("melt", () -> new Knowledge("melt", Dogma.id("textures/gui/knowledge/melt.png"), List.of(ASH.get())));
    RegistryObject<Knowledge> ROT = KNOWLEDGE_DEFERRED_REGISTER.register("rot", () -> new Knowledge("rot", Dogma.id("textures/gui/knowledge/rot.png"), List.of(MELT.get())));
    RegistryObject<Knowledge> CADUCEUS = KNOWLEDGE_DEFERRED_REGISTER.register("caduceus", () -> new Knowledge("caduceus", Dogma.id("textures/gui/knowledge/caduceus.png"), List.of(ROT.get())));

    RegistryObject<Knowledge> SOUL = KNOWLEDGE_DEFERRED_REGISTER.register("soul", () -> new Knowledge("soul", Dogma.id("textures/gui/knowledge/soul.png"), List.of(ALCHEMY.get())));
    RegistryObject<Knowledge> ESSENCE = KNOWLEDGE_DEFERRED_REGISTER.register("essence", () -> new Knowledge("essence", Dogma.id("textures/gui/knowledge/essence.png"), List.of(SOUL.get())));
    RegistryObject<Knowledge> FUSION = KNOWLEDGE_DEFERRED_REGISTER.register("fusion", () -> new Knowledge("fusion", Dogma.id("textures/gui/knowledge/fusion.png"), List.of(ESSENCE.get())));
    RegistryObject<Knowledge> MULTIPLICATION = KNOWLEDGE_DEFERRED_REGISTER.register("multiplication", () -> new Knowledge("multiplication", Dogma.id("textures/gui/knowledge/multiplication.png"), List.of(FUSION.get())));
    RegistryObject<Knowledge> LIFE = KNOWLEDGE_DEFERRED_REGISTER.register("life", () -> new Knowledge("life", Dogma.id("textures/gui/knowledge/life.png"), List.of(MULTIPLICATION.get())));

    RegistryObject<Knowledge> VOID = KNOWLEDGE_DEFERRED_REGISTER.register("void", () -> new Knowledge("void", Dogma.id("textures/gui/knowledge/void.png"), List.of(ALCHEMY.get())));
    RegistryObject<Knowledge> AMALGAM = KNOWLEDGE_DEFERRED_REGISTER.register("amalgam", () -> new Knowledge("amalgam", Dogma.id("textures/gui/knowledge/amalgam.png"), List.of(ALCHEMY.get())));
    RegistryObject<Knowledge> DISPOSITION = KNOWLEDGE_DEFERRED_REGISTER.register("disposition", () -> new Knowledge("disposition", Dogma.id("textures/gui/knowledge/disposition.png"), List.of(VOID.get(), AMALGAM.get())));
    RegistryObject<Knowledge> BALANCE = KNOWLEDGE_DEFERRED_REGISTER.register("balance", () -> new Knowledge("balance", Dogma.id("textures/gui/knowledge/balance.png"), List.of(DISPOSITION.get())));
    RegistryObject<Knowledge> PROJECTION = KNOWLEDGE_DEFERRED_REGISTER.register("projection", () -> new Knowledge("projection", Dogma.id("textures/gui/knowledge/projection.png"), List.of(BALANCE.get())));

    private static <T> Supplier<IForgeRegistry<T>> makeSyncedRegistry(DeferredRegister<T> deferredRegister) {
        return deferredRegister.makeRegistry(() -> new RegistryBuilder<T>().disableSaving());
    }
}
