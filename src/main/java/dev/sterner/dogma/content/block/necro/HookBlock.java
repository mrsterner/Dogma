package dev.sterner.dogma.content.block.necro;

import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.registry.DogmaBlockRegistry;
import dev.sterner.dogma.foundation.registry.DogmaParticleTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class HookBlock extends HorizontalDirectionalBlock implements EntityBlock {
    protected static final VoxelShape SHAPE = Block.box(5, 2.0, 5, 11, 16.0, 11);
    protected static final VoxelShape HOOKED_SHAPE = Block.box(4, 0.0, 4, 12, 16.0, 12);
    public boolean isMetal;

    public HookBlock(Properties settings, boolean isMetal) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        this.isMetal = isMetal;
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);
        if (pLevel.getBlockEntity(pPos) instanceof HookBlockEntity hookBlockEntity && !hookBlockEntity.getCorpseData().isEmpty() && hookBlockEntity.hookedAge < Constants.Values.BLEEDING) {
            for (int i = 0; i < pRandom.nextInt(1) + 1; ++i) {
                this.trySpawnDripParticles(pLevel, pPos, pState);
            }
        }
    }


    private void trySpawnDripParticles(Level pLevel, BlockPos pos, BlockState state) {
        if (state.getFluidState().isEmpty() && !(pLevel.random.nextFloat() < 0.3F)) {
            VoxelShape voxelshape = state.getCollisionShape(pLevel, pos);
            double d0 = voxelshape.max(Direction.Axis.Y);
            if (d0 >= 1.0D && !state.is(BlockTags.IMPERMEABLE)) {
                double d1 = voxelshape.min(Direction.Axis.Y);
                if (d1 > 0.0D) {
                    this.spawnParticle(pLevel, pos, voxelshape, (double) pos.getY() + d1 - 0.05D);
                } else {
                    BlockPos blockpos = pos.below();
                    BlockState blockstate = pLevel.getBlockState(blockpos);
                    VoxelShape voxelshape1 = blockstate.getCollisionShape(pLevel, blockpos);
                    double d2 = voxelshape1.max(Direction.Axis.Y);
                    if ((d2 < 1.0D || !blockstate.isCollisionShapeFullBlock(pLevel, blockpos)) && blockstate.getFluidState().isEmpty()) {
                        this.spawnParticle(pLevel, pos, voxelshape, (double) pos.getY() - 0.05D);
                    }
                }
            }

        }
    }

    private void spawnParticle(Level world, BlockPos pos, VoxelShape shape, double y) {
        this.spawnFluidParticle(world, (double) pos.getX() + shape.min(Direction.Axis.X), (double) pos.getX() + shape.max(Direction.Axis.X), (double) pos.getZ() + shape.min(Direction.Axis.Z), (double) pos.getZ() + shape.max(Direction.Axis.Z), y);
    }

    private void spawnFluidParticle(Level world, double minX, double maxX, double minZ, double maxZ, double y) {
        world.addParticle(DogmaParticleTypeRegistry.HANGING_BLOOD.get(), Mth.lerp(world.random.nextDouble(), minX, maxX), y, Mth.lerp(world.random.nextDouble(), minZ, maxZ), 0.0D, 0.0D, 0.0D);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return (tickerWorld, pos, tickerState, blockEntity) -> {
            if (blockEntity instanceof HookBlockEntity be) {
                be.tick(world, pos, state);
            }
        };
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            if (pLevel.getBlockEntity(pPos) instanceof HookBlockEntity hookBlockEntity) {
                return hookBlockEntity.onUse(pLevel, pState, pPos, pPlayer, pHand, false);
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }


    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pState.canSurvive(pLevel, pPos)) {
            pLevel.destroyBlock(pPos, true);
        }
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if (pDirection == Direction.UP && !pState.canSurvive(pLevel, pPos)) {
            pLevel.scheduleTick(pPos, DogmaBlockRegistry.ROPE.get(), 1);
        }
        if (pDirection != Direction.DOWN || !pNeighborState.is(DogmaBlockRegistry.ROPE.get()) && !pNeighborState.is(DogmaBlockRegistry.ROPE.get())) {
            return super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
        } else {
            return this.copyState(pState, this.defaultBlockState());
        }
    }

    protected BlockState copyState(BlockState from, BlockState to) {
        return to;
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.above()).isRedstoneConductor(pLevel, pPos) || pLevel.getBlockState(pPos.above()).is(DogmaBlockRegistry.ROPE.get());
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pLevel.getBlockEntity(pPos) instanceof HookBlockEntity hookBlockEntity && !hookBlockEntity.getCorpseData().isEmpty()) {
            return HOOKED_SHAPE;
        }
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HookBlockEntity(pos, state);
    }
}