package dev.sterner.dogma.content.block.necro;

import dev.sterner.dogma.foundation.registry.DogmaBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SulfurLayerBlock extends SnowLayerBlock {
    public SulfurLayerBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if (pState.is(this) && pState.getValue(LAYERS) > 1) {
            pLevel.setBlock(pPos, pState.setValue(LAYERS, pState.getValue(LAYERS) - 1), Block.UPDATE_ALL);
        }
        super.destroy(pLevel, pPos, pState);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState blockState = pLevel.getBlockState(pPos.below());
        return Block.isFaceFull(blockState.getCollisionShape(pLevel, pPos.below()), Direction.UP) || blockState.is(this) && blockState.getValue(LAYERS) == 8;
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return true;
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        boolean sulfur = pLevel.getBlockState(pPos.above()).is(DogmaBlockRegistry.SULFUR_PILE.get());
        if (pLevel.getBlockState(pPos.above()).isAir() || sulfur) {
            boolean bl = false;
            for (Direction direction : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
                if (pLevel.getBlockState(pPos.relative(direction)).is(Blocks.LAVA)) {
                    bl = true;
                    break;
                }
            }
            if (bl) {
                if (pState.getValue(LAYERS) < SnowLayerBlock.MAX_HEIGHT) {
                    pLevel.setBlockAndUpdate(pPos, DogmaBlockRegistry.SULFUR_PILE.get().defaultBlockState().setValue(LAYERS, Mth.clamp(1 + pState.getValue(LAYERS), 1, 8)));
                } else if (sulfur && pLevel.getBlockState(pPos.above()).getValue(LAYERS) <= 3) {
                    pLevel.setBlockAndUpdate(pPos.above(), DogmaBlockRegistry.SULFUR_PILE.get().defaultBlockState().setValue(LAYERS, 1 + pLevel.getBlockState(pPos.above()).getValue(LAYERS)));
                }
            }
        }
    }
}
