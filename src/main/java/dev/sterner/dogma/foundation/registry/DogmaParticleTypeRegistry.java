package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.client.particle.BloodDripParticle;
import dev.sterner.dogma.client.particle.BloodSplashParticle;
import dev.sterner.dogma.client.particle.ItemStackBeamParticleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.systems.particle.type.LodestoneParticleType;

public interface DogmaParticleTypeRegistry {
    DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Dogma.MODID);
    //start common
    RegistryObject<ItemStackBeamParticleType> ITEM_BEAM_PARTICLE = PARTICLES.register("item_beam_particle", ItemStackBeamParticleType::new);

    //end common
    //------------------------------------------------
    //start abyss

    //end abyss
    //------------------------------------------------
    //start necro
    RegistryObject<LodestoneParticleType> SPIRIT_FLAME_PARTICLE = PARTICLES.register("spirit_flame", LodestoneParticleType::new);
    RegistryObject<SimpleParticleType> HANGING_BLOOD = PARTICLES.register("hanging_blood", () -> new SimpleParticleType(true));
    RegistryObject<SimpleParticleType> FALLING_BLOOD = PARTICLES.register("falling_blood", () -> new SimpleParticleType(true));
    RegistryObject<SimpleParticleType> LANDING_BLOOD = PARTICLES.register("landing_blood", () -> new SimpleParticleType(true));
    RegistryObject<SimpleParticleType> SPLASHING_BLOOD = PARTICLES.register("splashing_blood", () -> new SimpleParticleType(true));

    //end necro
    //------------------------------------------------

    static void registerParticleFactory(RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(SPIRIT_FLAME_PARTICLE.get(), LodestoneParticleType.Factory::new);

        Minecraft.getInstance().particleEngine.register(ITEM_BEAM_PARTICLE.get(), new ItemStackBeamParticleType.Factory());
        //Minecraft.getInstance().particleEngine.register(ParticleTypes.DRIPPING_WATER, DripParticle::createWaterHangParticle);

        Minecraft.getInstance().particleEngine.register(HANGING_BLOOD.get(), BloodDripParticle::createBloodHangParticle);
        Minecraft.getInstance().particleEngine.register(FALLING_BLOOD.get(), BloodDripParticle::createBloodFallParticle);
        Minecraft.getInstance().particleEngine.register(LANDING_BLOOD.get(), BloodDripParticle::createBloodLandParticle);
        Minecraft.getInstance().particleEngine.register(SPLASHING_BLOOD.get(), BloodSplashParticle.Factory::new);
    }
}
