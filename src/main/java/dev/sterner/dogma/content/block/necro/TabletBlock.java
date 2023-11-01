package dev.sterner.dogma.content.block.necro;

import dev.sterner.dogma.api.BookBlock;
import dev.sterner.dogma.content.block_entity.necro.TabletBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TabletBlock extends BookBlock {
    public TabletBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TabletBlockEntity(pos, state);
    }
}