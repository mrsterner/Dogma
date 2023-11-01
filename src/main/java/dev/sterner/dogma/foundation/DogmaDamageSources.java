package dev.sterner.dogma.foundation;

import dev.sterner.dogma.Dogma;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface DogmaDamageSources {

    //start common

    //end common
    //------------------------------------------------
    //start abyss
    ResourceKey<DamageType> CURSE = ResourceKey.create(Registries.DAMAGE_TYPE, Dogma.id("curse"));
    //end abyss
    //------------------------------------------------
    //start necro
    ResourceKey<DamageType> SANGUINE = ResourceKey.create(Registries.DAMAGE_TYPE, Dogma.id("sanguine"));
    ResourceKey<DamageType> SACRIFICE = ResourceKey.create(Registries.DAMAGE_TYPE, Dogma.id("sacrifice"));

    //end necro
    //------------------------------------------------

    static DamageSource create(Level world, ResourceKey<DamageType> key, @Nullable Entity source, @Nullable Entity attacker) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), source, attacker);
    }

    static DamageSource create(Level world, ResourceKey<DamageType> key, @Nullable Entity attacker) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), attacker);
    }

    static DamageSource create(Level world, ResourceKey<DamageType> key) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
    }
}
