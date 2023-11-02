package dev.sterner.dogma.foundation.capability.necro;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.api.IHauler;
import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.DogmaPackets;
import dev.sterner.dogma.foundation.networking.necro.SyncNecroPlayerCapabilityDataPacket;
import dev.sterner.dogma.foundation.networking.necro.SyncNecroPlayerHaulerCapabilityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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

import java.util.Optional;

public class NecroPlayerHaulerDataCapability implements LodestoneCapability, IHauler {

    public CompoundTag corpseData = new CompoundTag();

    public static Capability<NecroPlayerHaulerDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public NecroPlayerHaulerDataCapability() {

    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(NecroPlayerHaulerDataCapability.class);
    }

    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            final NecroPlayerHaulerDataCapability capability = new NecroPlayerHaulerDataCapability();
            event.addCapability(Dogma.id("necro_player_hauler_data"), new LodestoneCapabilityProvider<>(NecroPlayerHaulerDataCapability.CAPABILITY, () -> capability));
        }
    }

    @Override
    public CompoundTag getCorpseData() {
        return corpseData;
    }

    @Override
    public LivingEntity getCorpseEntity(Level level){
        Optional<Entity> optionalEntity = EntityType.create(getCorpseData(), level);
        if (optionalEntity.isPresent() && optionalEntity.get() instanceof LivingEntity livingEntity) {
            return livingEntity;
        }
        return null;
    }

    @Override
    public void setCorpseEntity(Player player, LivingEntity corpse){
        CompoundTag nbtCompound = new CompoundTag();
        nbtCompound.putString("id", corpse.getEncodeId());
        corpse.save(nbtCompound);
        setCorpseData(nbtCompound);
        NecroPlayerHaulerDataCapability.syncTrackingAndSelf(player);
    }

    @Override
    public void setCorpseData(CompoundTag nbt) {
        this.corpseData = nbt;
    }

    public void clearCorpseData(Player player) {
        this.corpseData = new CompoundTag();
        NecroPlayerHaulerDataCapability.syncTrackingAndSelf(player);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put(Constants.Nbt.CORPSE_ENTITY, getCorpseData());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        setCorpseData(nbt.getCompound(Constants.Nbt.CORPSE_ENTITY));
    }

    @Override
    public void setHeadVisible(boolean visible) {

    }

    @Override
    public void setRArmVisible(boolean visible) {

    }

    @Override
    public void setLArmVisible(boolean visible) {

    }

    @Override
    public void setRLegVisible(boolean visible) {

    }

    @Override
    public void setLLegVisible(boolean visible) {

    }

    //-----------------OBLIGATORY_PLAYER_IMPLEMENTATION-----------------
    public static void playerClone(PlayerEvent.Clone event) {
        NecroPlayerHaulerDataCapability originalCapability = NecroPlayerHaulerDataCapability.getCapabilityOptional(event.getOriginal()).orElse(new NecroPlayerHaulerDataCapability());
        NecroPlayerHaulerDataCapability capability = NecroPlayerHaulerDataCapability.getCapabilityOptional(event.getEntity()).orElse(new NecroPlayerHaulerDataCapability());
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
        getCapabilityOptional(player).ifPresent(c -> DogmaPackets.DOGMA_CHANNEL.send(target, new SyncNecroPlayerHaulerCapabilityDataPacket(player.getUUID(), c.serializeNBT())));
    }

    public static LazyOptional<NecroPlayerHaulerDataCapability> getCapabilityOptional(LivingEntity entity) {
        return entity.getCapability(CAPABILITY);
    }

    public static NecroPlayerHaulerDataCapability getCapability(LivingEntity entity) {
        return entity.getCapability(CAPABILITY).orElse(new NecroPlayerHaulerDataCapability());
    }
    //----------END_OF_OBLIGATORY_PLAYER_IMPLEMENTATION-----------------

}