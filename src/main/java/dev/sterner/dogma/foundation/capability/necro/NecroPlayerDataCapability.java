package dev.sterner.dogma.foundation.capability.necro;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.api.DogmaPlayerCapability;
import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.DogmaPackets;
import dev.sterner.dogma.foundation.networking.necro.SyncNecroPlayerCapabilityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;
import team.lodestar.lodestone.systems.capability.LodestoneCapability;
import team.lodestar.lodestone.systems.capability.LodestoneCapabilityProvider;

public class NecroPlayerDataCapability extends DogmaPlayerCapability implements LodestoneCapability {

    private boolean isLich = false;

    public static Capability<NecroPlayerDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public NecroPlayerDataCapability() {

    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(NecroPlayerDataCapability.class);
    }



    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
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

    public boolean getLich() {
        return this.isLich;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        setLich(tag.getBoolean(Constants.Nbt.IS_LICH));
        return tag;
    }

    private void setLich(boolean aBoolean) {
        this.isLich = aBoolean;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        nbt.putBoolean(Constants.Nbt.IS_LICH, getLich());
    }

    //-----------------OBLIGATORY_PLAYER_IMPLEMENTATION-----------------
    public static void playerClone(PlayerEvent.Clone event) {
        NecroPlayerDataCapability originalCapability = NecroPlayerDataCapability.getCapabilityOptional(event.getOriginal()).orElse(new NecroPlayerDataCapability());
        NecroPlayerDataCapability capability = NecroPlayerDataCapability.getCapabilityOptional(event.getEntity()).orElse(new NecroPlayerDataCapability());
        capability.deserializeNBT(originalCapability.serializeNBT());
    }

    public static void syncPlayerCapability(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player player) {
            if (player.level() instanceof ServerLevel) {
                syncTracking(player);
            }
        }
    }

    public static void playerJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player instanceof ServerPlayer serverPlayer) {
                syncSelf(serverPlayer);
            }
        }
    }

    public static void syncSelf(ServerPlayer player) {
        sync(player, PacketDistributor.PLAYER.with(() -> player));
    }

    public static void syncTrackingAndSelf(Player player) {
        sync(player, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player));
    }

    public static void syncTracking(Player player) {
        sync(player, PacketDistributor.TRACKING_ENTITY.with(() -> player));
    }

    public static void sync(Player player, PacketDistributor.PacketTarget target) {
        getCapabilityOptional(player).ifPresent(c -> DogmaPackets.DOGMA_CHANNEL.send(target, new SyncNecroPlayerCapabilityDataPacket(player.getUUID(), c.serializeNBT())));
    }

    public static LazyOptional<NecroPlayerDataCapability> getCapabilityOptional(LivingEntity entity) {
        return entity.getCapability(CAPABILITY);
    }

    public static NecroPlayerDataCapability getCapability(LivingEntity entity) {
        return entity.getCapability(CAPABILITY).orElse(new NecroPlayerDataCapability());
    }
    //----------END_OF_OBLIGATORY_PLAYER_IMPLEMENTATION-----------------

}
