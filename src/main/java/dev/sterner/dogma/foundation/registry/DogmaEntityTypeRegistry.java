package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.api.FloatingItemEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface DogmaEntityTypeRegistry {
    DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Dogma.MODID);
    //start common

    RegistryObject<EntityType<FloatingItemEntity>> FLOATING_ITEM_ENTITY = ENTITY_TYPES.register("floating_item",
            () -> EntityType.Builder.of(FloatingItemEntity::new, MobCategory.MISC).sized(0.5F, 0.75F).clientTrackingRange(10)
                    .build(Dogma.id("floating_item").toString()));
    //end common
    //------------------------------------------------
    //start abyss

    //end abyss
    //------------------------------------------------
    //start necro

    //end necro
    //------------------------------------------------
}
