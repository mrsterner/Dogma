package dev.sterner.dogma.foundation.handler.abyss;

import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.DogmaTags;
import dev.sterner.dogma.foundation.abyss.curse.Curse;
import dev.sterner.dogma.foundation.abyss.curse.CurseUtils;
import dev.sterner.dogma.foundation.capability.abyss.AbyssLivingEntityDataCapability;
import dev.sterner.dogma.foundation.registry.DogmaRegistries;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag; 
import net.minecraft.resources.ResourceLocation; 
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

public class CurseHandler {
    private final int MAX_COOLDOWN = 2 * 20;
    private boolean isInCurse = false;
    private boolean isImmune = false;
    private Curse curse = DogmaRegistries.NONE.get();

    private int checkCooldown = 0;
    private int currentY;
    private TimeSpentOnY timeSpentOnY;

    public CurseHandler() {

    }

    public static void tick(LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();

        if (!livingEntity.getType().is(DogmaTags.CURSE_SUSCEPTIBLE)) {
            return;
        }

        AbyssLivingEntityDataCapability capability = AbyssLivingEntityDataCapability.getCapability(livingEntity);
        CurseHandler manager = capability.curseHandler;

        manager.checkCooldown++;

        manager.tryAddCurse(livingEntity);

        if (manager.isInCurse()) {
            manager.tryUpdateCurseIntensity(livingEntity);

            CurseUtils.updateLowestY(manager, livingEntity);

            //Common
            manager.curse.tickEffect(livingEntity.getCommandSenderWorld(), livingEntity);

            if (CurseUtils.checkAscending(manager, livingEntity)) {
                manager.curse.tickAscensionEffect(livingEntity.getCommandSenderWorld(), livingEntity);
            }

            //Client
            if (livingEntity.level() instanceof ClientLevel clientWorld) {
                manager.curse.tickClientEffect(clientWorld, livingEntity);
                if (CurseUtils.checkAscending(manager, livingEntity)) {
                    manager.curse.tickAscensionEffectClient(clientWorld, livingEntity);
                }
            }
        }
    }

    private void tryUpdateCurseIntensity(LivingEntity livingEntity) {
        Curse worldCurse = CurseUtils.getWorldCurse(livingEntity.level(), livingEntity.blockPosition());
        if (getCurse() != null && worldCurse != getCurse() && getCurse().getIntensity().getId() < worldCurse.getIntensity().getId()) {
            setCurse(worldCurse);
        }
    }

    private void tryAddCurse(LivingEntity livingEntity) {
        if (checkCooldown > MAX_COOLDOWN) {
            checkCooldown = 0;
            boolean bl = CurseUtils.checkIfInCurse(livingEntity.level(), livingEntity, livingEntity.blockPosition());
            if (bl != isInCurse()) {
                setInCurse(bl);
            }
        }
    }

    public void writeCurseToNbt(CompoundTag nbt) {
        nbt.putInt(Constants.Nbt.COOLDOWN, getCheckCooldown());
        nbt.putInt(Constants.Nbt.CURRENT_Y, getCurrentY());
        nbt.putBoolean(Constants.Nbt.IMMUNE, isImmune());
        nbt.putBoolean(Constants.Nbt.IS_IN_CURSE, isInCurse());

        if (getCurse() != null) {
            nbt.putString(Constants.Nbt.CURSE, DogmaRegistries.CURSE_REGISTRY.get().getKey(getCurse()).toString());
        }

        if (getTimeSpentOnY() != null) {
            nbt.putInt(Constants.Nbt.TIME_Y, getTimeSpentOnY().y());
            nbt.putLong(Constants.Nbt.TIME_AGE, getTimeSpentOnY().age());
        }
    }

    public void readCurseFromNbt(CompoundTag nbt) {
        setCheckCooldown(nbt.getInt(Constants.Nbt.COOLDOWN));
        setTimeSpentOnY(new TimeSpentOnY(nbt.getInt(Constants.Nbt.TIME_Y), nbt.getLong(Constants.Nbt.TIME_AGE)));
        setCurrentY(nbt.getInt(Constants.Nbt.CURRENT_Y));
        setImmune(nbt.getBoolean(Constants.Nbt.IMMUNE));
        setInCurse(nbt.getBoolean(Constants.Nbt.IS_IN_CURSE));
        Curse curse = DogmaRegistries.CURSE_REGISTRY.get().getValue(new ResourceLocation(nbt.getString(Constants.Nbt.CURSE)));
        if (curse != null) {
            setCurse(curse);
        }
    }

    public boolean isInCurse() {
        return isInCurse;
    }

    public void setInCurse(boolean inCurse) {
        isInCurse = inCurse;
    }

    public Curse getCurse() {
        return curse;
    }

    public void setCurse(Curse curse) {
        this.curse = curse;
    }

    public boolean isImmune() {
        return isImmune;
    }

    public void setImmune(boolean immune) {
        isImmune = immune;
    }

    public int getCurrentY() {
        return currentY;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public TimeSpentOnY getTimeSpentOnY() {
        return timeSpentOnY;
    }

    public void setTimeSpentOnY(TimeSpentOnY timeSpentOnY) {
        this.timeSpentOnY = timeSpentOnY;
    }

    public void setTimeSpentOnY(int y, long age) {
        setTimeSpentOnY(new TimeSpentOnY(y, age));
    }

    public int getCheckCooldown() {
        return checkCooldown;
    }

    public void setCheckCooldown(int checkCooldown) {
        this.checkCooldown = checkCooldown;
    }

    public record TimeSpentOnY(int y, long age) {
    }
}