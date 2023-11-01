package dev.sterner.dogma.foundation.networking;

import dev.sterner.dogma.foundation.capability.abyss.AbyssLevelDataCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;

import java.util.function.Supplier;

public class SyncLevelCapabilityPacket extends LodestoneClientPacket {
    private final CompoundTag tag;


    public SyncLevelCapabilityPacket(CompoundTag tag) {
        this.tag = tag;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(tag);
    }

    @OnlyIn(Dist.CLIENT)
    public void execute(Supplier<NetworkEvent.Context> context) {
        if (Minecraft.getInstance().level != null) {
            AbyssLevelDataCapability.getCapabilityOptional(Minecraft.getInstance().level).ifPresent(c -> c.deserializeNBT(tag));
        }
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, SyncLevelCapabilityPacket.class, SyncLevelCapabilityPacket::encode, SyncLevelCapabilityPacket::decode, SyncLevelCapabilityPacket::handle);
    }

    public static SyncLevelCapabilityPacket decode(FriendlyByteBuf buf) {
        return new SyncLevelCapabilityPacket(buf.readNbt());
    }
}