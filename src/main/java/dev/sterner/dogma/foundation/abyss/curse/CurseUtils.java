package dev.sterner.dogma.foundation.abyss.curse;

import dev.sterner.dogma.foundation.capability.abyss.AbyssLevelDataCapability;
import dev.sterner.dogma.foundation.handler.abyss.CurseHandler;
import dev.sterner.dogma.foundation.registry.DogmaRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class CurseUtils {
    public boolean canBeAbyssChunk(Level world, ChunkPos chunkPos) {
        Optional<AbyssLevelDataCapability> worldDataCapabilityOptional = null;//TODO
        if (worldDataCapabilityOptional.isPresent()) {
            AbyssLevelDataCapability worldAbyssComponent = worldDataCapabilityOptional.get();
            boolean bl = tooCloseAbyss(worldAbyssComponent, chunkPos);
            if (!bl) {
                tryGenerateAbyss(worldAbyssComponent, chunkPos);
            }
        }
        return false;
    }

    private void tryGenerateAbyss(AbyssLevelDataCapability world, ChunkPos chunkPos) {

    }

    private boolean tooCloseAbyss(AbyssLevelDataCapability world, ChunkPos chunkPos) {

        return false;//TODO cheat
    }

    //TODO implement
    public static Curse getWorldCurse(Level world, BlockPos blockPos) {
        return DogmaRegistries.NONE.get();
    }

    //TODO implement
    public static boolean checkIfInCurse(Level world, LivingEntity livingEntity, BlockPos blockPos) {
        return false;
    }

    public static boolean checkAscending(CurseHandler handler, LivingEntity livingEntity) {
        if (handler.getTimeSpentOnY() == null) {
            return false;
        }

        if (livingEntity.tickCount - handler.getTimeSpentOnY().age() > 20 * 10) {
            handler.setTimeSpentOnY(livingEntity.getBlockY(), livingEntity.tickCount);
        }

        return livingEntity.getBlockY() - handler.getTimeSpentOnY().y() > 8;
    }

    public static void updateLowestY(CurseHandler handler, LivingEntity livingEntity) {
        if (handler.getTimeSpentOnY() == null) {
            handler.setTimeSpentOnY(livingEntity.getBlockY(), livingEntity.tickCount);
        }

        int y = livingEntity.blockPosition().getY();
        if (y < handler.getTimeSpentOnY().y()) {
            handler.setTimeSpentOnY(y, handler.getTimeSpentOnY().age());
        }
    }
}
