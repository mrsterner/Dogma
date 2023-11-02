package dev.sterner.dogma.content.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import dev.sterner.dogma.api.AbstractSkullBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class WallSkullBlock extends AbstractSkullBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final Map<Direction, VoxelShape> FACING_TO_SHAPE = Maps.newEnumMap(
            ImmutableMap.of(
                    Direction.NORTH,
                    Block.box(4.0, 4.0, 8.0, 12.0, 12.0, 16.0),
                    Direction.SOUTH,
                    Block.box(4.0, 4.0, 0.0, 12.0, 12.0, 8.0),
                    Direction.EAST,
                    Block.box(0.0, 4.0, 4.0, 8.0, 12.0, 12.0),
                    Direction.WEST,
                    Block.box(8.0, 4.0, 4.0, 16.0, 12.0, 12.0)
            )
    );

    public WallSkullBlock(SkullType type, Properties pProperties) {
        super(type, pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return FACING_TO_SHAPE.get(pState.getValue(FACING));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockState = this.defaultBlockState();
        var blockView = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        Direction[] directions = pContext.getNearestLookingDirections();

        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction2 = direction.getOpposite();
                blockState = blockState.setValue(FACING, direction2);
                if (!blockView.getBlockState(blockPos.relative(direction)).canBeReplaced(pContext)) {
                    return blockState;
                }
            }
        }

        return null;
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }
}
