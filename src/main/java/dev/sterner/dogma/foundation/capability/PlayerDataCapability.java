package dev.sterner.dogma.foundation.capability;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.DogmaPackets;
import dev.sterner.dogma.foundation.networking.SyncPlayerCapabilityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;
import team.lodestar.lodestone.systems.capability.LodestoneCapability;
import team.lodestar.lodestone.systems.capability.LodestoneCapabilityProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class PlayerDataCapability implements LodestoneCapability {

    public static Capability<PlayerDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public List<Ability> abilityList = new ArrayList<>();

    public PlayerDataCapability() {
        abilityList.add(new Ability(Ability.Type.HEALTH, 0));
        abilityList.add(new Ability(Ability.Type.SATURATION, 0));
        abilityList.add(new Ability(Ability.Type.EXPERIENCE, 0));
        abilityList.add(new Ability(Ability.Type.RESISTANCE, 0));
        abilityList.add(new Ability(Ability.Type.AGGRESSION, 0));
        abilityList.add(new Ability(Ability.Type.SANITY, 0));
        abilityList.add(new Ability(Ability.Type.MOB_EFFECT_SPONGE, 0));
        abilityList.add(new Ability(Ability.Type.REPUTATION, 0));
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerDataCapability.class);
    }

    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            final PlayerDataCapability capability = new PlayerDataCapability();
            event.addCapability(Dogma.id("player_data"), new LodestoneCapabilityProvider<>(PlayerDataCapability.CAPABILITY, () -> capability));
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        Ability.writeAbilitiesToNbt(tag, abilityList);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        Ability.readAbilitiesFromNbt(nbt, abilityList);
    }

    //-----------------OBLIGATORY_PLAYER_IMPLEMENTATION-----------------
    public static void playerJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player instanceof ServerPlayer serverPlayer) {
                syncSelf(serverPlayer);
            }
        }
    }

    public static void playerClone(PlayerEvent.Clone event) {
        PlayerDataCapability originalCapability = PlayerDataCapability.getCapabilityOptional(event.getOriginal()).orElse(new PlayerDataCapability());
        PlayerDataCapability capability = PlayerDataCapability.getCapabilityOptional(event.getEntity()).orElse(new PlayerDataCapability());
        capability.deserializeNBT(originalCapability.serializeNBT());
    }

    public static void syncPlayerCapability(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player player) {
            if (player.level() instanceof ServerLevel) {
                syncTracking(player);
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
        getCapabilityOptional(player).ifPresent(c -> DogmaPackets.DOGMA_CHANNEL.send(target, new SyncPlayerCapabilityDataPacket(player.getUUID(), c.serializeNBT())));
    }

    public static LazyOptional<PlayerDataCapability> getCapabilityOptional(LivingEntity entity) {
        return entity.getCapability(CAPABILITY);
    }

    public static PlayerDataCapability getCapability(LivingEntity entity) {
        return entity.getCapability(CAPABILITY).orElse(new PlayerDataCapability());
    }
    //----------END_OF_OBLIGATORY_PLAYER_IMPLEMENTATION-----------------

    public static class Ability {
        private final Type type;
        private int level;

        private Ability(Type type, int level){
            this.type = type;
            this.level = level;
        }

        public static Ability create(Type type, int level) {
            return new Ability(type, level);
        }

        public static void writeAbilitiesToNbt(CompoundTag nbt, List<Ability> abilities) {
            ListTag listTag = new ListTag();
            for (Ability ability : abilities) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putInt("Type", ability.type.id);
                compoundtag.putInt("Level", ability.level);
                listTag.add(compoundtag);
            }
            nbt.put("Abilities", listTag);
        }

        public static void readAbilitiesFromNbt(CompoundTag nbt, List<Ability> abilities) {
            abilities.clear();
            ListTag listTag = nbt.getList("Abilities", 10);
            for(int i = 0; i < listTag.size(); ++i) {
                CompoundTag compoundtag = listTag.getCompound(i);
                int typeId = compoundtag.getInt("Type");
                int level = compoundtag.getInt("Level");
                abilities.add(new Ability(Type.byId(typeId), level));
            }
        }

        public int getLevel() {
            return level;
        }

        public Type getType() {
            return type;
        }

        public enum Type {

            HEALTH(0,-5, 5),
            SATURATION(1,-5, 5),
            EXPERIENCE(2,-5, 5),
            RESISTANCE(3,-5, 5),
            AGGRESSION(4,-5, 5),
            SANITY(5,-5, 5),
            MOB_EFFECT_SPONGE(6,-5, 5),
            REPUTATION(7,-5, 5);

            private static final IntFunction<Type> BY_ID = ByIdMap.continuous(Type::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);

            private final int id;
            private final int lower;
            private final int upper;

            Type(int id, int lowerBound, int upperBound){
                this.lower = lowerBound;
                this.upper = upperBound;
                this.id = id;
            }

            public static Type byId(int id) {
                return BY_ID.apply(id);
            }

            public int getId() {
                return this.id;
            }

            public int getLower() {
                return lower;
            }

            public int getUpper() {
                return upper;
            }
        }
    }
}