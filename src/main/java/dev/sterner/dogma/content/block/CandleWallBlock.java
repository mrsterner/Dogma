package dev.sterner.dogma.content.block;

import dev.sterner.dogma.foundation.registry.DogmaItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CandleWallBlock extends Block {
    public static final IntegerProperty HEIGHT = IntegerProperty.create("height", 0, 4);
    public static final float[] PARTICLE_HEIGHT = {0, 10 / 16f, 13 / 16f, 15 / 16f, 17 / 16f};
    public static final float[] VOXEL_HEIGHT = {4, 8, 11, 13, 15};

    public CandleWallBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(BlockStateProperties.LIT, false)
                        .setValue(HEIGHT, 4)
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
        );
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack handStack = pPlayer.getMainHandItem();
        if (pHand == InteractionHand.MAIN_HAND) {
            if (handStack.is(DogmaItemRegistry.FAT.get()) || handStack.is(Items.HONEYCOMB)) {
                if (!pState.getValue(BlockStateProperties.LIT) && pState.getValue(HEIGHT) < 4) {
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(HEIGHT, pState.getValue(HEIGHT) + 1));
                    if (!pPlayer.isCreative()) {
                        handStack.shrink(1);
                    }
                    return InteractionResult.CONSUME;
                }
            } else if (handStack.isEmpty() && pState.getValue(BlockStateProperties.LIT)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(BlockStateProperties.LIT, false));
                pLevel.playSound(null, pPos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);

            } else if (pState.getValue(HEIGHT) > 0 && (handStack.is(Items.FLINT_AND_STEEL) || handStack.is(Items.FIRE_CHARGE))) {
                pLevel.playSound(pPlayer, pPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, pLevel.getRandom().nextFloat() * 0.4F + 0.8F);
                pLevel.setBlock(pPos, pState.setValue(BlockStateProperties.LIT, true), Block.UPDATE_ALL | Block.UPDATE_ALL_IMMEDIATE);
                pLevel.gameEvent(pPlayer, GameEvent.BLOCK_CHANGE, pPos);
                handStack.hurtAndBreak(1, pPlayer, p -> p.broadcastBreakEvent(pHand));
                return InteractionResult.CONSUME;
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(6.0, 0.0, 6.0, 10.0, VOXEL_HEIGHT[pState.getValue(HEIGHT)], 10.0);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        Direction direction = pState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockPos blockPos = pPos.relative(direction.getOpposite());
        BlockState blockState = pLevel.getBlockState(blockPos);
        return blockState.isFaceSturdy(pLevel, blockPos, direction);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockState = this.defaultBlockState();
        var worldView = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        Direction[] directions = pContext.getNearestLookingDirections();

        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction2 = direction.getOpposite();
                blockState = blockState.setValue(BlockStateProperties.HORIZONTAL_FACING, direction2);
                if (blockState.canSurvive(worldView, blockPos)) {
                    return blockState;
                }
            }
        }
        return null;
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        return pDirection.getOpposite() == pState.getValue(BlockStateProperties.HORIZONTAL_FACING) && !pState.canSurvive(pLevel, pPos) ? Blocks.AIR.defaultBlockState() : pState;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.LIT, HEIGHT, BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(BlockStateProperties.LIT) && pState.getValue(HEIGHT) > 0) {
            double d = (double) pPos.getX() + 0.5;
            double e = (double) pPos.getY() + PARTICLE_HEIGHT[pState.getValue(HEIGHT)];
            double f = (double) pPos.getZ() + 0.5;
            pLevel.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
            pLevel.addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);
        }
    }
}