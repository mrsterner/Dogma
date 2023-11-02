package dev.sterner.dogma.foundation.capability.necro;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.api.DogmaApi;
import dev.sterner.dogma.api.event.LivingDeathTickEvent;
import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.DogmaPackets;
import dev.sterner.dogma.foundation.networking.necro.SyncNecroCorpseCapabilityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;
import org.intellij.lang.annotations.Identifier;
import team.lodestar.lodestone.systems.capability.LodestoneCapability;
import team.lodestar.lodestone.systems.capability.LodestoneCapabilityProvider;

public class NecroCorpseDataCapability implements LodestoneCapability {

    public boolean isCorpse = false;
    public boolean shouldDie = false;
    private DamageSource damageSource;

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

    public static void tickDeath(LivingDeathTickEvent event) {
        LivingEntity livingEntity = event.getLivingEntity();
        NecroCorpseDataCapability capability = NecroCorpseDataCapability.getCapability(livingEntity);
        boolean isCorpse = capability.isCorpse;
        if ((isCorpse || DogmaApi.isButchering(livingEntity))) {
            Level level = livingEntity.level();
            if (livingEntity instanceof Player) {
                capability.isCorpse(true);
            }
            if (livingEntity instanceof Mob) {
                capability.isCorpse(true);
                ++livingEntity.deathTime;
                if (livingEntity.deathTime == 1) {
                    if (livingEntity.isOnFire()) {
                        livingEntity.clearFire();
                    }
                    if (livingEntity.getVehicle() != null) {
                        livingEntity.stopRiding();
                    }
                }

                AABB corpseBox = new AABB(livingEntity.getX() - (livingEntity.getBbWidth() / 2.0F), livingEntity.getY() - (livingEntity.getBbWidth() / 2.0F), livingEntity.getZ() - (livingEntity.getBbWidth() / 2.0F), livingEntity.getX() + (livingEntity.getBbWidth() / 1.5F), livingEntity.getY() + (livingEntity.getBbWidth() / 1.5F), livingEntity.getZ() + (livingEntity.getBbWidth() / 1.5F));
                if ((livingEntity.getDimensions(Pose.STANDING).height < 1.0F && livingEntity.getDimensions(Pose.STANDING).width < 1.0F) || (livingEntity.getDimensions(Pose.STANDING).width / livingEntity.getDimensions(Pose.STANDING).height) > 1.395F) {
                    livingEntity.setBoundingBox(corpseBox);
                } else {
                    livingEntity.setBoundingBox(corpseBox.move(livingEntity.calculateViewVector(0F, livingEntity.yBodyRot).yRot(-30.0F)));
                }

                if (livingEntity.deathTime < 20 * 60 * 5 && !capability.shouldDie) {
                    event.setCanceled(true);
                } else {
                    MinecraftServer server = level.getServer();
                    if (server != null && capability.damageSource != null) {
                        ResourceLocation identifier = livingEntity.getLootTable();
                        LootTable lootTable = server.getLootData().getLootTable(identifier);
                        LootParams.Builder lootparams$builder = (new LootParams.Builder((ServerLevel)livingEntity.level())).withParameter(LootContextParams.THIS_ENTITY, livingEntity).withParameter(LootContextParams.ORIGIN, livingEntity.position()).withParameter(LootContextParams.DAMAGE_SOURCE, livingEntity.getLastDamageSource()).withOptionalParameter(LootContextParams.KILLER_ENTITY, livingEntity.getLastDamageSource().getEntity()).withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, livingEntity.getLastDamageSource().getDirectEntity());

                        lootTable.getRandomItems(lootparams$builder.create(LootContextParamSets.ENTITY), livingEntity::spawnAtLocation);
                    }
                }
            }
        }

    }

    public static void dropLoot(LivingDropsEvent livingDropsEvent) {
        LivingEntity livingEntity = livingDropsEvent.getEntity();
        NecroCorpseDataCapability capability = NecroCorpseDataCapability.getCapability(livingEntity);

        boolean isCorpse = capability.isCorpse;
        if ((isCorpse || DogmaApi.isButchering(livingEntity))) {
            capability.damageSource = livingEntity.getLastDamageSource();
            livingDropsEvent.setCanceled(true);
        }
    }

    public static void playerClone(PlayerEvent.Clone event) {
        NecroCorpseDataCapability capabilityOld = NecroCorpseDataCapability.getCapability(event.getOriginal());
        NecroCorpseDataCapability capability = NecroCorpseDataCapability.getCapability(event.getEntity());
        if (capabilityOld.isCorpse) {
            capability.isCorpse(false);
        }
    }

    public static void applyDamage(LivingDamageEvent event) {
        NecroCorpseDataCapability capability = NecroCorpseDataCapability.getCapability(event.getEntity());
        if (capability.isCorpse) {
            capability.shouldDie = true;
            sync(event.getEntity());
        }

    }

    public void isCorpse(boolean isCorpse) {
        this.isCorpse = isCorpse;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean(Constants.Nbt.IS_CORPSE, isCorpse);
        nbt.putBoolean(Constants.Nbt.SHOULD_DIE, shouldDie);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.isCorpse = nbt.getBoolean(Constants.Nbt.IS_CORPSE);
        this.shouldDie = nbt.getBoolean(Constants.Nbt.SHOULD_DIE);
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
