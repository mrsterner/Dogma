package dev.sterner.dogma.foundation;

import dev.sterner.dogma.Dogma;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public interface DogmaTags {
    TagKey<EntityType<?>> CURSE_SUSCEPTIBLE = TagKey.create(Registries.ENTITY_TYPE, Dogma.id("curse_susceptible"));
}