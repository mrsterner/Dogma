package dev.sterner.dogma.foundation.capability.necro;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.DogmaPackets;
import dev.sterner.dogma.foundation.networking.abyss.SyncAbyssLivingCapabilityDataPacket;
import dev.sterner.dogma.foundation.networking.necro.SyncNecroLivingCapabilityDataPacket;
import dev.sterner.dogma.foundation.util.DogmaUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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

import java.util.List;
import java.util.UUID;

public class NecroLivingEntityDataCapability implements LodestoneCapability {
    public final float[] ENTANGLE_STRENGTH = {0.05f, 0.10f, 0.15f, 0.20f, 0.25f, 0.30f, 0.35f, 0.40f, 0.45f, 0.50f, 0.55f, 0.60f, 0.65f, 0.70f, 0.75f, 0.80f};

    private float morphine$accumulatedDamage = 0;
    private float adrenaline$bonusDamage = 0;
    private int entangledEntityId = 0;
    private Vec3 ritualPos;

    public static Capability<NecroLivingEntityDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public NecroLivingEntityDataCapability() {

    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(NecroLivingEntityDataCapability.class);
    }

    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            final NecroLivingEntityDataCapability capability = new NecroLivingEntityDataCapability();
            event.addCapability(Dogma.id("necro_living_data"), new LodestoneCapabilityProvider<>(NecroLivingEntityDataCapability.CAPABILITY, () -> capability));
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat(Constants.Nbt.MORPHINE, getMorphine$accumulatedDamage());
        nbt.putFloat(Constants.Nbt.ADRENALINE, getAdrenaline$bonusDamage());
        nbt.putInt(Constants.Nbt.ENTANGLED, getEntangledEntityId());
        if (getRitualPos() != null) {
            nbt.put(Constants.Nbt.RITUAL_POS, DogmaUtils.fromVec3d(getRitualPos()));
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        setMorphine$accumulatedDamage(nbt.getFloat(Constants.Nbt.MORPHINE));
        setAdrenaline$bonusDamage(nbt.getFloat(Constants.Nbt.ADRENALINE));
        setEntangledEntityId(nbt.getInt(Constants.Nbt.ENTANGLED));
        if (nbt.contains(Constants.Nbt.RITUAL_POS)) {
            setRitualPos(DogmaUtils.toVec3d(nbt.getCompound(Constants.Nbt.RITUAL_POS)));
        }
    }

    public float getEntangleStrength(LivingEntity source, LivingEntity target, boolean isSource) {
        int i = 6;
        List<TagKey<EntityType<?>>> list = List.of(Constants.Tags.SOUL_WEAK, Constants.Tags.SOUL_REGULAR, Constants.Tags.SOUL_STRONG);

        ResourceKey<Level> sourceDim = source.level().dimension();
        ResourceKey<Level> targetDim = target.level().dimension();

        if (sourceDim != null && targetDim != null && !sourceDim.equals(targetDim)) {
            i -= 2;
        }

        if (source instanceof Player player && target instanceof TamableAnimal tameable) {
            i = entangleTameablePlayer(i, player, tameable);
        } else if (target instanceof Player player && source instanceof TamableAnimal tameable) {
            i = entangleTameablePlayer(i, player, tameable);
        }

        if (source instanceof Player player && target instanceof Monster hostileEntity) {
            i = entangleHostilePlayer(i, player, hostileEntity);
        } else if (target instanceof Player player && source instanceof Monster hostileEntity) {
            i = entangleHostilePlayer(i, player, hostileEntity);
        }

        if (source instanceof Player playerEntity && target instanceof Villager villagerEntity) {
            i = entangleVillagerPlayer(i, playerEntity, villagerEntity);
        } else if (target instanceof Player playerEntity && source instanceof Villager villagerEntity) {
            i = entangleVillagerPlayer(i, playerEntity, villagerEntity);
        }

        for (TagKey<EntityType<?>> tag : list) {
            if (tag.equals(Constants.Tags.SOUL_WEAK)) {
                if (source.getType().is(tag) || target.getType().is(tag)) {
                    i++;
                }
            } else if (tag.equals(Constants.Tags.SOUL_REGULAR)) {
                if (source.getType().is(tag) || target.getType().is(tag)) {
                    i += 2;
                }
            } else if (tag.equals(Constants.Tags.SOUL_STRONG)) {
                if (source.getType().is(tag) || target.getType().is(tag)) {
                    i += 3;
                }
            }
        }

        float strength = ENTANGLE_STRENGTH[Mth.clamp(i, 0, ENTANGLE_STRENGTH.length)];

        return isSource ? 1 - strength : strength;
    }

    public int entangleVillagerPlayer(int i, Player player, Villager villagerEntity) {
        int r = villagerEntity.getPlayerReputation(player);
        if (r <= -100) {
            i -= 2;
        } else if (r > 50) {
            i += 2;
        }

        return i;
    }

    public int entangleHostilePlayer(int i, Player player, Monster hostileEntity) {
        LivingEntity livingEntity1 = hostileEntity.getTarget();
        NecroPlayerDataCapability c = NecroPlayerDataCapability.getCapability(player);
        if (c.getLich() && hostileEntity.isInvertedHealAndHarm()) {
            i += 3;
        } else if (livingEntity1 != null && livingEntity1.getUUID() == player.getUUID()) {
            i -= 3;
        }

        return i;
    }

    public int entangleTameablePlayer(int i, Player player, TamableAnimal tameable) {
        UUID uuid = tameable.getOwnerUUID();
        if (uuid != null && uuid == player.getUUID()) {
            i += 3;
        }

        return i;
    }

    public int getEntangledEntityId() {
        return entangledEntityId;
    }

    public void setEntangledEntityId(int entangledEntityId) {
        this.entangledEntityId = entangledEntityId;
        syncAbility();
    }

    public float getMorphine$accumulatedDamage() {
        return morphine$accumulatedDamage;
    }

    public void setMorphine$accumulatedDamage(float morphine$accumulatedDamage) {
        this.morphine$accumulatedDamage = morphine$accumulatedDamage;
        syncAbility();
    }

    public void increaseMorphine$accumulatedDamage(float damage) {
        this.setMorphine$accumulatedDamage(getMorphine$accumulatedDamage() + damage);
    }

    public float getAdrenaline$bonusDamage() {
        return adrenaline$bonusDamage;
    }

    public void setAdrenaline$bonusDamage(float adrenaline$bonusDamage) {
        this.adrenaline$bonusDamage = adrenaline$bonusDamage;
        syncAbility();
    }

    public void increaseAdrenaline$bonusDamage(float damage) {
        setAdrenaline$bonusDamage(getAdrenaline$bonusDamage() + damage);
    }

    public Vec3 getRitualPos() {
        return ritualPos;
    }

    public void setRitualPos(Vec3 ritualPos) {
        this.ritualPos = ritualPos;
        syncAbility();
    }

    private void syncAbility() {

    }

    public static void syncEntityCapability(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity livingEntity) {
            if (livingEntity.level() instanceof ServerLevel) {
                NecroLivingEntityDataCapability.sync(livingEntity);
            }
        }
    }

    public static void sync(LivingEntity entity) {
        getCapabilityOptional(entity).ifPresent(
                c -> DogmaPackets.DOGMA_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                        new SyncNecroLivingCapabilityDataPacket(entity.getId(), c.serializeNBT())));
    }

    public static LazyOptional<NecroLivingEntityDataCapability> getCapabilityOptional(LivingEntity entity) {
        return entity.getCapability(CAPABILITY);
    }

    public static NecroLivingEntityDataCapability getCapability(LivingEntity entity) {
        return entity.getCapability(CAPABILITY).orElse(new NecroLivingEntityDataCapability());
    }
}
