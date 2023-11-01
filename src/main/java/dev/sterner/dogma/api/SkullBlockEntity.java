package dev.sterner.dogma.api;

import dev.sterner.dogma.foundation.registry.DogmaBlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SkullBlockEntity extends BlockEntity {
    public SkullBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(DogmaBlockEntityTypeRegistry.SKULL.get(), pPos, pBlockState);
    }
}
