package dev.sterner.dogma.foundation.capability.necro;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.DogmaPackets;
import dev.sterner.dogma.foundation.networking.necro.SyncNecroCorpseCapabilityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;
import team.lodestar.lodestone.systems.capability.LodestoneCapability;
import team.lodestar.lodestone.systems.capability.LodestoneCapabilityProvider;

public class NecroCorpseDataCapability implements LodestoneCapability {

    public boolean isCorpse = false;

    public static Capability<NecroCorpseDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public NecroCorpseDataCapability() {

    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(NecroCorpseDataCapability.class);
    }

    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            final NecroCorpseDataCapability capability = new NecroCorpseDataCapability();
            event.addCapability(Dogma.id("necro_corpse_data"), new LodestoneCapabilityProvider<>(NecroCorpseDataCapability.CAPABILITY, () -> capability));
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean(Constants.Nbt.IS_CORPSE, isCorpse);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.isCorpse = nbt.getBoolean(Constants.Nbt.IS_CORPSE);
    }

    public static void syncEntityCapability(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity livingEntity) {
            if (livingEntity.level() instanceof ServerLevel) {
                NecroCorpseDataCapability.sync(livingEntity);
            }
        }
    }

    public static void sync(LivingEntity entity) {
        getCapabilityOptional(entity).ifPresent(
                c -> DogmaPackets.DOGMA_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                        new SyncNecroCorpseCapabilityDataPacket(entity.getId(), c.serializeNBT())));
    }

    public static LazyOptional<NecroCorpseDataCapability> getCapabilityOptional(LivingEntity entity) {
        return entity.getCapability(CAPABILITY);
    }

    public static NecroCorpseDataCapability getCapability(LivingEntity entity) {
        return entity.getCapability(CAPABILITY).orElse(new NecroCorpseDataCapability());
    }
}
