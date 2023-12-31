package dev.sterner.dogma.foundation.event;

import dev.sterner.dogma.api.event.LivingDeathTickEvent;
import dev.sterner.dogma.content.block.RopeBlock;
import dev.sterner.dogma.content.block.necro.HookBlock;
import dev.sterner.dogma.foundation.capability.PlayerDataCapability;
import dev.sterner.dogma.foundation.capability.abyss.AbyssLevelDataCapability;
import dev.sterner.dogma.foundation.capability.abyss.AbyssLivingEntityDataCapability;
import dev.sterner.dogma.foundation.capability.necro.NecroCorpseDataCapability;
import dev.sterner.dogma.foundation.capability.necro.NecroLivingEntityDataCapability;
import dev.sterner.dogma.foundation.capability.necro.NecroPlayerDataCapability;
import dev.sterner.dogma.foundation.capability.necro.NecroPlayerHaulerDataCapability;
import dev.sterner.dogma.foundation.event.events.NecroInteractEvents;
import dev.sterner.dogma.foundation.handler.abyss.CradleHandler;
import dev.sterner.dogma.foundation.handler.abyss.CurseHandler;
import dev.sterner.dogma.foundation.listener.MeatToEntityDataReloadListener;
import dev.sterner.dogma.foundation.registry.DogmaBlockRegistry;
import dev.sterner.dogma.foundation.registry.DogmaItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Mod.EventBusSubscriber
public class DogmaRuntimeEvents {

    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        AbyssLivingEntityDataCapability.attachEntityCapability(event);
        NecroPlayerDataCapability.attachEntityCapability(event);
        PlayerDataCapability.attachEntityCapability(event);
        NecroCorpseDataCapability.attachEntityCapability(event);
        NecroLivingEntityDataCapability.attachEntityCapability(event);
        NecroPlayerHaulerDataCapability.attachEntityCapability(event);
    }

    @SubscribeEvent
    public static void attachWorldCapability(AttachCapabilitiesEvent<Level> event) {
        AbyssLevelDataCapability.attachWorldCapability(event);
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        CurseHandler.tick(event);
        CradleHandler.tick(event);
        AbyssLivingEntityDataCapability.tick(event);
    }

    @SubscribeEvent
    public static void registerListeners(AddReloadListenerEvent event) {
        MeatToEntityDataReloadListener.register(event);
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        AbyssLivingEntityDataCapability.syncEntityCapability(event);
        NecroPlayerDataCapability.syncPlayerCapability(event);
        PlayerDataCapability.syncPlayerCapability(event);
        NecroCorpseDataCapability.syncEntityCapability(event);
        NecroLivingEntityDataCapability.syncEntityCapability(event);
        NecroPlayerHaulerDataCapability.syncPlayerCapability(event);
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        PlayerDataCapability.playerJoin(event);
        NecroPlayerDataCapability.playerJoin(event);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerDataCapability.playerClone(event);
        NecroPlayerDataCapability.playerClone(event);
        NecroCorpseDataCapability.playerClone(event);
    }

    @SubscribeEvent
    public static void dropEvent(LivingDropsEvent livingDropsEvent) {
        AbyssLivingEntityDataCapability.onDeath(livingDropsEvent);
        NecroCorpseDataCapability.dropLoot(livingDropsEvent);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void reviveEvent(LivingDeathEvent event){
        NecroPlayerDataCapability.tryUseExtraLives(event);
    }

    @SubscribeEvent
    public static void applyDamage(LivingDamageEvent event){
        NecroLivingEntityDataCapability.applyDamage(event);
    }

    @SubscribeEvent
    public static void tickDeath(LivingDeathTickEvent event){
        NecroCorpseDataCapability.tickDeath(event);
    }

    @SubscribeEvent
    public static void playerLeftClick(PlayerInteractEvent.RightClickBlock event){
        NecroInteractEvents.init(event);
    }

    @SubscribeEvent
    public static void playerInteract(PlayerInteractEvent.EntityInteractSpecific event){
        NecroInteractEvents.init(event);
    }
}
