package dev.sterner.dogma.content.block_entity.necro;

import dev.sterner.dogma.foundation.registry.DogmaBlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public class NecroTableBlockEntity extends LodestoneBlockEntity {
    public NecroTableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public NecroTableBlockEntity(BlockPos pos, BlockState state) {
        this(DogmaBlockEntityTypeRegistry.NECRO.get(), pos, state);
    }
}
