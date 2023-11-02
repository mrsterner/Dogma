package dev.sterner.dogma.foundation.capability.necro;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.api.IHauler;
import dev.sterner.dogma.api.knowledge.Knowledge;
import dev.sterner.dogma.api.knowledge.KnowledgeData;
import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.DogmaPackets;
import dev.sterner.dogma.foundation.networking.necro.SyncNecroPlayerCapabilityDataPacket;
import dev.sterner.dogma.foundation.registry.DogmaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
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
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.C;
import team.lodestar.lodestone.systems.capability.LodestoneCapability;
import team.lodestar.lodestone.systems.capability.LodestoneCapabilityProvider;

import java.util.*;

public class NecroPlayerDataCapability implements LodestoneCapability {

    private boolean isLich = false;
    private static final int MAX_POINTS = 64;

    private boolean isAlchemist = false;
    private final Set<KnowledgeData> knowledgeData = new HashSet<>();

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

    public Set<KnowledgeData> getKnowledgeData() {
        return knowledgeData;
    }

    public boolean clearData(Player player) {
        boolean bl = !getKnowledgeData().isEmpty();
        getKnowledgeData().clear();
        syncTrackingAndSelf(player);
        return bl;
    }

    public boolean increaseKnowledgePoints(Player player, Knowledge knowledge, int amount) {
        for (KnowledgeData kd : knowledgeData) {
            if (kd.knowledge().equals(knowledge)) {
                int currentPoints = kd.points();
                int newPoints = currentPoints + amount;
                if (newPoints <= MAX_POINTS) {
                    setKnowledgePoint(player, kd, newPoints);
                    return true;
                }
            }
        }
        return false;
    }

    private void setKnowledgePoint(Player player, KnowledgeData kd, int newPoints) {
        if (knowledgeData.contains(kd)) {
            knowledgeData.remove(kd);
            knowledgeData.add(new KnowledgeData(kd.knowledge(), newPoints));
        }
        syncTrackingAndSelf(player);
    }

    public void setKnowledgePoint(Player player, Knowledge knowledge, int points) {
        for (KnowledgeData kd : knowledgeData) {
            if (kd.knowledge().equals(knowledge)) {
                knowledgeData.remove(kd);
                knowledgeData.add(new KnowledgeData(knowledge, points));
                break;
            }
        }
        syncTrackingAndSelf(player);
    }

    public boolean addKnowledge(Player player, Knowledge knowledge) {
        boolean canAddKnowledge = true;
        List<Knowledge> k = getKnowledgeData().stream().map(KnowledgeData::knowledge).toList();
        if (k.contains(knowledge)) {
            return false;
        }
        for (Knowledge child : knowledge.children) {
            if (!k.contains(child)) {
                canAddKnowledge = false;
                break;
            }
        }
        if (canAddKnowledge) {
            getKnowledgeData().add(new KnowledgeData(knowledge, 0));
        }

        syncTrackingAndSelf(player);
        return canAddKnowledge;
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

    private void setLich(boolean aBoolean) {
        this.isLich = aBoolean;
    }

    public boolean isAlchemist() {
        return isAlchemist;
    }

    public void setAlchemist(Player player, boolean alchemist) {
        isAlchemist = alchemist;
        syncTrackingAndSelf(player);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean(Constants.Nbt.IS_LICH, getLich());

        ListTag knowledgeList = new ListTag();
        for (KnowledgeData knowledgeData : getKnowledgeData()) {
            CompoundTag nbtCompound = new CompoundTag();
            nbtCompound.putString(Constants.Nbt.KNOWLEDGE, knowledgeData.knowledge().identifier);
            nbtCompound.putInt(Constants.Nbt.POINTS, knowledgeData.points());
            knowledgeList.add(nbtCompound);
        }
        nbt.put(Constants.Nbt.KNOWLEDGE_DATA, knowledgeList);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        setLich(nbt.getBoolean(Constants.Nbt.IS_LICH));

        getKnowledgeData().clear();

        ListTag nbtList = nbt.getList(Constants.Nbt.KNOWLEDGE_DATA, CompoundTag.TAG_COMPOUND);
        List<KnowledgeData> knowledgeDataList = new ArrayList<>();

        for (int i = 0; i < nbtList.size(); ++i) {
            CompoundTag nbtCompound = nbtList.getCompound(i);
            ResourceLocation id = Dogma.id(nbtCompound.getString(Constants.Nbt.KNOWLEDGE));
            if (DogmaRegistries.KNOWLEDGE_REGISTRY.get().containsKey(id)) {
                Knowledge knowledge = DogmaRegistries.KNOWLEDGE_REGISTRY.get().getValue(id);
                int points = nbtCompound.getInt(Constants.Nbt.POINTS);
                knowledgeDataList.add(new KnowledgeData(knowledge, points));
            }
        }

        getKnowledgeData().addAll(knowledgeDataList);
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
