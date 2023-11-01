package dev.sterner.dogma.content.block;

import dev.sterner.dogma.api.AbstractSkullBlock;

public class WallSkullBlock extends AbstractSkullBlock {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    private static final Map<Direction, VoxelShape> FACING_TO_SHAPE = Maps.newEnumMap(
            ImmutableMap.of(
                    Direction.NORTH,
                    Block.createCuboidShape(4.0, 4.0, 8.0, 12.0, 12.0, 16.0),
                    Direction.SOUTH,
                    Block.createCuboidShape(4.0, 4.0, 0.0, 12.0, 12.0, 8.0),
                    Direction.EAST,
                    Block.createCuboidShape(0.0, 4.0, 4.0, 8.0, 12.0, 12.0),
                    Direction.WEST,
                    Block.createCuboidShape(8.0, 4.0, 4.0, 16.0, 12.0, 12.0)
            )
    );

    public WallSkullBlock(SkullType type, Properties pProperties) {
        super(type, pProperties);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return FACING_TO_SHAPE.get(state.get(FACING));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState();
        BlockView blockView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        Direction[] directions = ctx.getPlacementDirections();

        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction2 = direction.getOpposite();
                blockState = blockState.with(FACING, direction2);
                if (!blockView.getBlockState(blockPos.offset(direction)).canReplace(ctx)) {
                    return blockState;
                }
            }
        }

        return null;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}