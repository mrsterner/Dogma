package dev.sterner.dogma.foundation;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.networking.SyncLevelCapabilityPacket;
import dev.sterner.dogma.foundation.networking.SyncLivingCapabilityDataPacket;
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

        SyncLivingCapabilityDataPacket.register(DOGMA_CHANNEL, index++);
        SyncLevelCapabilityPacket.register(DOGMA_CHANNEL, index++);
    }
}
