package dev.sterner.dogma.foundation.capability.abyss;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.DogmaDamageSources;
import dev.sterner.dogma.foundation.DogmaPackets;
import dev.sterner.dogma.foundation.handler.abyss.CradleHandler;
import dev.sterner.dogma.foundation.handler.abyss.CurseHandler;
import dev.sterner.dogma.foundation.networking.abyss.SyncAbyssLivingCapabilityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;
import team.lodestar.lodestone.systems.capability.LodestoneCapability;
import team.lodestar.lodestone.systems.capability.LodestoneCapabilityProvider;

public class AbyssLivingEntityDataCapability implements LodestoneCapability {

    public CurseHandler curseHandler = new CurseHandler();
    public CradleHandler cradleHandler = new CradleHandler();

    public boolean isRevived = false;
    public long revivedTimer = -1;

    public static Capability<AbyssLivingEntityDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public AbyssLivingEntityDataCapability() {

    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(AbyssLivingEntityDataCapability.class);
    }

    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            final AbyssLivingEntityDataCapability capability = new AbyssLivingEntityDataCapability();
            event.addCapability(Dogma.id("abyss_living_data"), new LodestoneCapabilityProvider<>(AbyssLivingEntityDataCapability.CAPABILITY, () -> capability));
        }
    }

    /**
     * When a mob is spawned with the curse warding box, set its life to be temporary.
     *
     * @param livingEntity revived entity
     * @param time         time in ticks the mob will stay alive
     */
    public static void setRevived(LivingEntity livingEntity, long time) {
        AbyssLivingEntityDataCapability capability = getCapability(livingEntity);
        capability.isRevived = true;
        capability.revivedTimer = time;
        if (livingEntity.level() instanceof ServerLevel) {
            sync(livingEntity);
        }
    }

    /**
     * removes or resets the revived status, not killing the entity in the process
     *
     * @param livingEntity entity to remove status from
     */
    public static void removeRevived(LivingEntity livingEntity) {
        AbyssLivingEntityDataCapability capability = getCapability(livingEntity);
        capability.isRevived = false;
        capability.revivedTimer = -1;
        if (livingEntity.level() instanceof ServerLevel) {
            sync(livingEntity);
        }
    }

    public static void tick(LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Mob) {
            AbyssLivingEntityDataCapability capability = getCapability(livingEntity);
            if (capability.isRevived) {
                if (capability.revivedTimer > 0) {
                    capability.revivedTimer--;
                } else {
                    livingEntity.hurt(DogmaDamageSources.create(livingEntity.level(), DogmaDamageSources.CURSE), Float.MAX_VALUE);
                }
                if (livingEntity.level() instanceof ServerLevel) {
                    sync(livingEntity);
                }
            }
        }
    }

    public static void onDeath(LivingDropsEvent livingDropsEvent) {
        LivingEntity livingEntity = livingDropsEvent.getEntity();
        AbyssLivingEntityDataCapability capability = AbyssLivingEntityDataCapability.getCapability(livingEntity);
        if (capability.isRevived) {
            livingDropsEvent.getDrops().clear();
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(Constants.Nbt.IS_REVIVED, this.isRevived);
        tag.putLong(Constants.Nbt.REVIVED_TIMER, this.revivedTimer);

        curseHandler.writeCurseToNbt(tag);
        cradleHandler.writeCurseToNbt(tag);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.isRevived = tag.getBoolean(Constants.Nbt.IS_REVIVED);
        this.revivedTimer = tag.getInt(Constants.Nbt.REVIVED_TIMER);

        curseHandler.readCurseFromNbt(tag);
        cradleHandler.readCurseFromNbt(tag);
    }

    public static void syncEntityCapability(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity livingEntity) {
            if (livingEntity.level() instanceof ServerLevel) {
                AbyssLivingEntityDataCapability.sync(livingEntity);
            }
        }
    }

    public static void sync(LivingEntity entity) {
        getCapabilityOptional(entity).ifPresent(
                c -> DogmaPackets.DOGMA_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                        new SyncAbyssLivingCapabilityDataPacket(entity.getId(), c.serializeNBT())));
    }

    public static LazyOptional<AbyssLivingEntityDataCapability> getCapabilityOptional(LivingEntity entity) {
        return entity.getCapability(CAPABILITY);
    }

    public static AbyssLivingEntityDataCapability getCapability(LivingEntity entity) {
        return entity.getCapability(CAPABILITY).orElse(new AbyssLivingEntityDataCapability());
    }
}