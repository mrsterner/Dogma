package dev.sterner.dogma.foundation.capability.necro;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.DogmaPackets;
import dev.sterner.dogma.foundation.capability.abyss.AbyssLivingEntityDataCapability;
import dev.sterner.dogma.foundation.networking.SyncLivingCapabilityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Vector3d;
import team.lodestar.lodestone.systems.capability.LodestoneCapability;
import team.lodestar.lodestone.systems.capability.LodestoneCapabilityProvider;

public class NecroPlayerDataCapability implements LodestoneCapability {

    public static Capability<NecroPlayerDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public NecroPlayerDataCapability() {

    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(NecroPlayerDataCapability.class);
    }

    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            final NecroPlayerDataCapability capability = new NecroPlayerDataCapability();
            event.addCapability(Dogma.id("necro_player_data"), new LodestoneCapabilityProvider<>(NecroPlayerDataCapability.CAPABILITY, () -> capability));
        }
    }

    public static void tryUseExtraLives(LivingDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player player) {
            NecroPlayerDataCapability capability = NecroPlayerDataCapability.getCapability(livingEntity);
            if (capability.getKakuzu() > 0) {
                capability.decreaseKakuzuBuffLevel();
                player.setHealth(player.getMaxHealth());
                player.clearFire();
                player.removeAllEffects();
                player.setDeltaMovement(Vec3.ZERO);
                player.fallDistance = 0;
                player.setTicksFrozen(0);
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.5f, 0.75f);
            } 
        }
    }

    private void decreaseKakuzuBuffLevel() {
    }

    private int getKakuzu() {
        return 0;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }

    public static void syncEntityCapability(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity livingEntity) {
            if (livingEntity.level() instanceof ServerLevel) {
                NecroPlayerDataCapability.sync(livingEntity);
            }
        }
    }

    public static void sync(LivingEntity entity) {
        getCapabilityOptional(entity).ifPresent(
                c -> DogmaPackets.DOGMA_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                        new SyncLivingCapabilityDataPacket(entity.getId(), c.serializeNBT())));
    }

    public static LazyOptional<NecroPlayerDataCapability> getCapabilityOptional(LivingEntity entity) {
        return entity.getCapability(CAPABILITY);
    }

    public static NecroPlayerDataCapability getCapability(LivingEntity entity) {
        return entity.getCapability(CAPABILITY).orElse(new NecroPlayerDataCapability());
    }
}
