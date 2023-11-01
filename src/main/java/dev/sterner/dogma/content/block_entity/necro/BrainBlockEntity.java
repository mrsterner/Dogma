package dev.sterner.dogma.content.block_entity.necro;

import dev.sterner.dogma.foundation.registry.DogmaBlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public class BrainBlockEntity extends LodestoneBlockEntity {
    public BrainBlockEntity(BlockPos pos, BlockState state) {
        super(DogmaBlockEntityTypeRegistry.BRAIN.get(), pos, state);
    }
}