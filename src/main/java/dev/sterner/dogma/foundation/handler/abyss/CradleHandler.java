package dev.sterner.dogma.foundation.handler.abyss;

import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.capability.abyss.AbyssLivingEntityDataCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

public class CradleHandler {
    private boolean isHost = false;

    public CradleHandler() {

    }

    public static void tick(LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();
        AbyssLivingEntityDataCapability capability = AbyssLivingEntityDataCapability.getCapability(livingEntity);
        CradleHandler cradleManager = capability.cradleHandler;
        if (cradleManager.isHost()) {
            //TODO Do Tick
        }
    }

    public void writeCurseToNbt(CompoundTag tag) {
        tag.putBoolean(Constants.Nbt.COOLDOWN, isHost());
    }

    public void readCurseFromNbt(CompoundTag tag) {
        setHost(tag.getBoolean(Constants.Nbt.HOST));
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }
}
