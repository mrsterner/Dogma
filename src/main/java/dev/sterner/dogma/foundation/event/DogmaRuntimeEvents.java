package dev.sterner.dogma.foundation.event;

import dev.sterner.dogma.foundation.capability.PlayerDataCapability;
import dev.sterner.dogma.foundation.capability.abyss.AbyssLevelDataCapability;
import dev.sterner.dogma.foundation.capability.abyss.AbyssLivingEntityDataCapability;
import dev.sterner.dogma.foundation.capability.necro.NecroPlayerDataCapability;
import dev.sterner.dogma.foundation.handler.abyss.CradleHandler;
import dev.sterner.dogma.foundation.handler.abyss.CurseHandler;
import dev.sterner.dogma.foundation.listener.MeatToEntityDataReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DogmaRuntimeEvents {

    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        AbyssLivingEntityDataCapability.attachEntityCapability(event);
        NecroPlayerDataCapability.attachEntityCapability(event);
        PlayerDataCapability.attachEntityCapability(event);
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
        NecroPlayerDataCapability.syncEntityCapability(event);
        PlayerDataCapability.syncEntityCapability(event);
    }

    @SubscribeEvent
    public static void dropEvent(LivingDropsEvent livingDropsEvent) {
        AbyssLivingEntityDataCapability.onDeath(livingDropsEvent);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void reviveEvent(LivingDeathEvent event){
        NecroPlayerDataCapability.tryUseExtraLives(event);
    }
}
