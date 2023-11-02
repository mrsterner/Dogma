package dev.sterner.dogma.foundation.util;

import dev.sterner.dogma.client.particle.ItemStackBeamParticleOptions;
import dev.sterner.dogma.foundation.registry.DogmaParticleTypeRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ParticleUtils {
    /**
     * generates the beam of particles from the pedestals to the ritual center
     *
     * @param from        blockpos from
     * @param to          blockpos to
     * @param serverWorld serverWorld
     * @param itemStack   itemstack for particle effect
     */
    public static void spawnItemParticleBeam(Vec3 from, Vec3 to, ServerLevel serverWorld, ItemStack itemStack) {
        Vec3 directionVector = to.subtract(from);

        double x = from.x() + (serverWorld.random.nextDouble() * 0.2D) + 0.4D;
        double y = from.y() + (serverWorld.random.nextDouble() * 0.2D) + 1.2D;
        double z = from.z() + (serverWorld.random.nextDouble() * 0.2D) + 0.4D;
        if (!itemStack.isEmpty()) {
            serverWorld.sendParticles(
                    new ItemStackBeamParticleOptions(DogmaParticleTypeRegistry.ITEM_BEAM_PARTICLE.get(), itemStack),
                    x,
                    y,
                    z,
                    0,
                    directionVector.x,
                    directionVector.y,
                    directionVector.z,
                    0.10D);
        }
    }

    /**
     * Handles the output items particle effect
     *
     * @param serverWorld serverWorld
     * @param x           coordinate for sound and particle
     * @param y           coordinate for sound and particle
     * @param z           coordinate for sound and particle
     */
    public static void generateItemParticle(ServerLevel serverWorld, double x, double y, double z, List<ItemStack> items) {
        if (items != null) {
            for (ItemStack output : items) {
                for (int i = 0; i < items.size() * 2; i++) {
                    serverWorld.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, output),
                            x + ((serverWorld.random.nextDouble() / 2) - 0.25),
                            y + ((serverWorld.random.nextDouble() / 2) - 0.25),
                            z + ((serverWorld.random.nextDouble() / 2) - 0.25),
                            0,
                            1 * ((serverWorld.random.nextDouble() / 2) - 0.25),
                            1 * ((serverWorld.random.nextDouble() / 2) - 0.25),
                            1 * ((serverWorld.random.nextDouble() / 2) - 0.25),
                            0);
                }
            }
        }
    }
}
