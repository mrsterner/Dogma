package dev.sterner.dogma.foundation;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.networking.KnowledgeC2SPacket;
import dev.sterner.dogma.foundation.networking.SyncPlayerCapabilityDataPacket;
import dev.sterner.dogma.foundation.networking.abyss.SyncAbyssLevelCapabilityPacket;
import dev.sterner.dogma.foundation.networking.abyss.SyncAbyssLivingCapabilityDataPacket;
import dev.sterner.dogma.foundation.networking.necro.SyncNecroCorpseCapabilityDataPacket;
import dev.sterner.dogma.foundation.networking.necro.SyncNecroLivingCapabilityDataPacket;
import dev.sterner.dogma.foundation.networking.necro.SyncNecroPlayerCapabilityDataPacket;
import dev.sterner.dogma.foundation.networking.necro.SyncNecroPlayerHaulerCapabilityDataPacket;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = Dogma.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DogmaPackets {

    public static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel DOGMA_CHANNEL = NetworkRegistry.newSimpleChannel(Dogma.id("main"), () -> DogmaPackets.PROTOCOL_VERSION, DogmaPackets.PROTOCOL_VERSION::equals, DogmaPackets.PROTOCOL_VERSION::equals);

    @SubscribeEvent
    public static void registerNetworkStuff(FMLCommonSetupEvent event) {
        int index = 0;

        //Abyss
        SyncAbyssLivingCapabilityDataPacket.register(DOGMA_CHANNEL, index++);
        SyncAbyssLevelCapabilityPacket.register(DOGMA_CHANNEL, index++);

        //Necro
        SyncNecroCorpseCapabilityDataPacket.register(DOGMA_CHANNEL, index++);
        SyncNecroLivingCapabilityDataPacket.register(DOGMA_CHANNEL, index++);
        SyncNecroPlayerCapabilityDataPacket.register(DOGMA_CHANNEL, index++);
        SyncNecroPlayerHaulerCapabilityDataPacket.register(DOGMA_CHANNEL, index++);

        KnowledgeC2SPacket.register(DOGMA_CHANNEL, index++);

        //Core
        SyncPlayerCapabilityDataPacket.register(DOGMA_CHANNEL, index++);
    }
}
