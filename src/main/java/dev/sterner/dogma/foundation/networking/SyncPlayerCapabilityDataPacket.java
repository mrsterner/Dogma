package dev.sterner.dogma.foundation.networking;

import dev.sterner.dogma.foundation.capability.PlayerDataCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncPlayerCapabilityDataPacket extends LodestoneClientPacket {
    private final UUID uuid;
    private final CompoundTag tag;

    public SyncPlayerCapabilityDataPacket(UUID uuid, CompoundTag tag) {
        this.uuid = uuid;
        this.tag = tag;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
        buf.writeNbt(tag);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        Player player = Minecraft.getInstance().level.getPlayerByUUID(uuid);
        PlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> c.deserializeNBT(tag));
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, SyncPlayerCapabilityDataPacket.class, SyncPlayerCapabilityDataPacket::encode, SyncPlayerCapabilityDataPacket::decode, SyncPlayerCapabilityDataPacket::handle);
    }

    public static SyncPlayerCapabilityDataPacket decode(FriendlyByteBuf buf) {
        return new SyncPlayerCapabilityDataPacket(buf.readUUID(), buf.readNbt());
    }
}