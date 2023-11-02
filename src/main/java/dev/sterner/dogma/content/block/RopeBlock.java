package dev.sterner.dogma.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.swing.text.html.BlockView;
import java.util.random.RandomGenerator;

public class RopeBlock extends Block {

    protected static final VoxelShape MIDDLE_SHAPE = Block.box(6.5, 0.0, 6.5, 9.5, 16.0, 9.5);
    protected static final VoxelShape BOTTOM_SHAPE = Block.box(6.5, 2.0, 6.5, 9.5, 16.0, 9.5);
    public static final EnumProperty<Rope> ROPE = EnumProperty.create("rope", Rope.class);

    public RopeBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(ROPE, Rope.BOTTOM));
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.above()).isRedstoneConductor(pLevel, pPos) || pLevel.getBlockState(pPos.above()).is(this);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pState.canSurvive(pLevel, pPos)) {
            pLevel.destroyBlock(pPos, true);
        }
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(ROPE, Rope.BOTTOM);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if (pDirection == Direction.UP && !pState.canSurvive(pLevel, pPos)) {
            pLevel.scheduleTick(pPos, this, 1);
        }
        if (pDirection != Direction.DOWN || !pNeighborState.is(this) && !pNeighborState.is(this)) {
            return super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
        } else {
            return this.copyState(pState, this.defaultBlockState());
        }
    }

    protected BlockState copyState(BlockState from, BlockState to) {
        return to;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        boolean bl2 = pState.getValue(ROPE) == Rope.MIDDLE;
        return bl2 ? MIDDLE_SHAPE : BOTTOM_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ROPE);
    }

    public enum Rope implements StringRepresentable {
        MIDDLE,
        BOTTOM;

        public String toString() {
            return this.getSerializedName();
        }

        @Override
        public String getSerializedName() {
            return this == MIDDLE ? "middle" : "bottom";
        }
    }
}