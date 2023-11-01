package dev.sterner.dogma.foundation.networking.necro;

import dev.sterner.dogma.foundation.capability.PlayerDataCapability;
import dev.sterner.dogma.foundation.capability.necro.NecroPlayerDataCapability;
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

public class SyncNecroPlayerCapabilityDataPacket extends LodestoneClientPacket {
    private final UUID uuid;
    private final CompoundTag tag;

    public SyncNecroPlayerCapabilityDataPacket(UUID uuid, CompoundTag tag) {
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
        NecroPlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> c.deserializeNBT(tag));
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, SyncNecroPlayerCapabilityDataPacket.class, SyncNecroPlayerCapabilityDataPacket::encode, SyncNecroPlayerCapabilityDataPacket::decode, SyncNecroPlayerCapabilityDataPacket::handle);
    }

    public static SyncNecroPlayerCapabilityDataPacket decode(FriendlyByteBuf buf) {
        return new SyncNecroPlayerCapabilityDataPacket(buf.readUUID(), buf.readNbt());
    }
}