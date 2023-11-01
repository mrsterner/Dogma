package dev.sterner.dogma.foundation.event;

import dev.sterner.dogma.foundation.capability.PlayerDataCapability;
import dev.sterner.dogma.foundation.capability.abyss.AbyssLevelDataCapability;
import dev.sterner.dogma.foundation.capability.abyss.AbyssLivingEntityDataCapability;
import dev.sterner.dogma.foundation.capability.necro.NecroCorpseDataCapability;
import dev.sterner.dogma.foundation.capability.necro.NecroLivingEntityDataCapability;
import dev.sterner.dogma.foundation.capability.necro.NecroPlayerDataCapability;
import dev.sterner.dogma.foundation.registry.DogmaParticleTypeRegistry;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DogmaSetupEvents {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        AbyssLivingEntityDataCapability.registerCapabilities(event);
        AbyssLevelDataCapability.registerCapabilities(event);
        NecroPlayerDataCapability.registerCapabilities(event);
        PlayerDataCapability.registerCapabilities(event);
        NecroCorpseDataCapability.registerCapabilities(event);
        NecroLivingEntityDataCapability.registerCapabilities(event);
    }
}