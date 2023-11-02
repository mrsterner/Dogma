package dev.sterner.dogma.foundation.networking;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.api.knowledge.Knowledge;
import dev.sterner.dogma.foundation.capability.necro.NecroPlayerDataCapability;
import dev.sterner.dogma.foundation.registry.DogmaRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.systems.network.LodestoneServerPacket;

import java.util.function.Supplier;

public class KnowledgeC2SPacket {
    protected String resourceLocation;

    public KnowledgeC2SPacket(Knowledge knowledge){
        this.resourceLocation = knowledge.identifier;
    }

    public KnowledgeC2SPacket(String knowledge){
        this.resourceLocation = knowledge;
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, KnowledgeC2SPacket.class, KnowledgeC2SPacket::encode, KnowledgeC2SPacket::decode, KnowledgeC2SPacket::handle);
    }

    public static void handle(KnowledgeC2SPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer sender = ctx.get().getSender();
            NecroPlayerDataCapability capability = NecroPlayerDataCapability.getCapability(sender);
            ResourceLocation id = Dogma.id(msg.resourceLocation);
            if (DogmaRegistries.KNOWLEDGE_REGISTRY.get().containsKey(id)) {
                Knowledge knowledge = DogmaRegistries.KNOWLEDGE_REGISTRY.get().getValue(id);
                if (knowledge != null) {
                    if (capability.addKnowledge(sender, knowledge)) {
                        sender.level().playSound(null, sender.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.5f, 0.9f);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static KnowledgeC2SPacket decode(FriendlyByteBuf buf) {
        return new KnowledgeC2SPacket(buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(resourceLocation);
    }
}
