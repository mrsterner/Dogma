package dev.sterner.dogma.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class LivingDeathTickEvent extends Event {

    private final LivingEntity livingEntity;

    public LivingDeathTickEvent(LivingEntity livingEntity){
        this.livingEntity = livingEntity;
    }

    public LivingEntity getLivingEntity() {
        return livingEntity;
    }
}
