package dev.sterner.dogma.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import team.lodestar.lodestone.systems.particle.options.AbstractWorldParticleOptions;

public class ItemStackBeamParticleOptions extends AbstractWorldParticleOptions {
    public static Codec<ItemStackBeamParticleOptions> brokenItemCodec(ParticleType<?> type) {
        return Codec.unit(() -> new ItemStackBeamParticleOptions(type));
    }

    public final ItemStack stack;

    public ItemStackBeamParticleOptions(ParticleType<?> type, ItemStack stack) {
        super(type);
        this.stack = stack;
    }

    public ItemStackBeamParticleOptions(ParticleType<?> type) {
        this(type, null);
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
    }

    @Override
    public String writeToString() {
        return "";
    }

    public static final Deserializer<ItemStackBeamParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public ItemStackBeamParticleOptions fromCommand(ParticleType<ItemStackBeamParticleOptions> type, StringReader reader) {
            return new ItemStackBeamParticleOptions(type);
        }

        @Override
        public ItemStackBeamParticleOptions fromNetwork(ParticleType<ItemStackBeamParticleOptions> type, FriendlyByteBuf buf) {
            return new ItemStackBeamParticleOptions(type);
        }
    };
}
