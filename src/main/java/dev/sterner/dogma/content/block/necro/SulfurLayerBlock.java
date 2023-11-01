package dev.sterner.dogma.content.block.necro;

import net.minecraft.world.level.block.SnowLayerBlock;

public class SulfurLayerBlock extends SnowLayerBlock {
    public SulfurLayerBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (state.isOf(this) && state.get(LAYERS) > 1) {
            world.setBlockState(pos, state.with(LAYERS, state.get(LAYERS) - 1), Block.NOTIFY_ALL);
        }
        super.onBroken(world, pos, state);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.down());
        return Block.isFaceFullSquare(blockState.getCollisionShape(world, pos.down()), Direction.UP) || blockState.isOf(this) && blockState.get(LAYERS) == 8;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
        boolean sulfur = world.getBlockState(pos.up()).isOf(BotDObjects.SULFUR_PILE);
        if (world.getBlockState(pos.up()).isAir() || sulfur) {
            boolean bl = false;
            for (Direction direction : Properties.HORIZONTAL_FACING.getValues()) {
                if (world.getBlockState(pos.offset(direction)).isOf(Blocks.LAVA)) {
                    bl = true;
                    break;
                }
            }
            if (bl) {
                if (state.get(LAYERS) < MAX_LAYERS) {
                    world.setBlockState(pos, BotDObjects.SULFUR_PILE.getDefaultState().with(LAYERS, MathHelper.clamp(1 + state.get(LAYERS), 1, 8)));
                } else if (sulfur && world.getBlockState(pos.up()).get(LAYERS) <= 3) {
                    world.setBlockState(pos.up(), BotDObjects.SULFUR_PILE.getDefaultState().with(LAYERS, 1 + world.getBlockState(pos.up()).get(LAYERS)));
                }
            }
        }
    }
}
