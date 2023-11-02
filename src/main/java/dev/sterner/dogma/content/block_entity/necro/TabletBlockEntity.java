package dev.sterner.dogma.content.block_entity.necro;

import dev.sterner.dogma.foundation.registry.DogmaBlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public class TabletBlockEntity extends LodestoneBlockEntity {
    public TabletBlockEntity(BlockPos pos, BlockState state) {
        super(DogmaBlockEntityTypeRegistry.TABLET.get(), pos, state);
    }
}