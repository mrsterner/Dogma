package dev.sterner.dogma.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IRitual {

    /**
     * When all items and all sacrifices has been consumed, this ticks until end of ritual
     *
     * @param level       level
     * @param blockPos    ritual origin
     * @param blockEntity ritualBlockEntity
     */
    void tick(Level level, BlockPos blockPos, NecroTableBlockEntity blockEntity);

    /**
     * Should run once after ritual has ended
     *
     * @param level       level
     * @param blockPos    ritual origin
     * @param blockEntity ritualBlockEntity
     */
    void onStopped(Level level, BlockPos blockPos, NecroTableBlockEntity blockEntity);


    void reset();
}