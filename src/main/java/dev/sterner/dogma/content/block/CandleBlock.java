package dev.sterner.dogma.content.block;

import dev.sterner.dogma.foundation.registry.DogmaItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CandleBlock extends Block {
    public static final IntegerProperty HEIGHT = IntegerProperty.create("height", 0, 4);
    public static final float[] PARTICLE_HEIGHT = {0, 7 / 16f, 10 / 16f, 12 / 16f, 14 / 16f};
    public static final float[] VOXEL_HEIGHT = {2, 5, 8, 10, 12};
    public static final float[] VOXEL_WIDTH = {6, 5, 4};
    //return Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, VOXEL_HEIGHT[state.get(HEIGHT)], 10.0);
    public static final IntegerProperty CANDLES = IntegerProperty.create("candles", 1, 3);

    public CandleBlock(Properties settings) {
        super(settings
                .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 12 : 0).noOcclusion()
                .strength(0.1F)
                .sound(SoundType.CANDLE));

        this.registerDefaultState(
                this.getStateDefinition()
                        .any()
                        .setValue(BlockStateProperties.LIT, false)
                        .setValue(HEIGHT, 4)
                        .setValue(CANDLES, 1)
        );
    }

    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        return !pUseContext.isSecondaryUseActive() && pUseContext.getItemInHand().getItem() == this.asItem() && pState.getValue(CANDLES) < 3 || super.canBeReplaced(pState, pUseContext);
    }



    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockState = pContext.getLevel().getBlockState(pContext.getClickedPos());
        if (blockState.is(this)) {
            return blockState.cycle(CANDLES);
        } else {
            return super.getStateForPlacement(pContext);
        }
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
                pLevel.setBlock(pPos, pState.setValue(BlockStateProperties.LIT, true), Block.UPDATE_ALL | Block.UPDATE_IMMEDIATE);
                pLevel.gameEvent(pPlayer, GameEvent.BLOCK_CHANGE, pPos);
                handStack.hurtAndBreak(1, pPlayer, p -> p.broadcastBreakEvent(pHand));
                return InteractionResult.CONSUME;
            }
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(
                VOXEL_WIDTH[pState.getValue(CANDLES) - 1],
                0.0,
                VOXEL_WIDTH[pState.getValue(CANDLES) - 1],
                16 - VOXEL_WIDTH[pState.getValue(CANDLES) - 1],
                VOXEL_HEIGHT[pState.getValue(HEIGHT)],
                16 - VOXEL_WIDTH[pState.getValue(CANDLES) - 1]);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.LIT, HEIGHT, CANDLES);
        super.createBlockStateDefinition(pBuilder);
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return pState.getValue(HEIGHT) > 0 && pState.getValue(BlockStateProperties.LIT);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(HEIGHT) > 0 && pState.getValue(BlockStateProperties.LIT) && pRandom.nextFloat() < 0.001F) {
            if (pRandom.nextFloat() < 0.001F) {
                BlockState newState = pState.setValue(HEIGHT, pState.getValue(HEIGHT) - 1).setValue(BlockStateProperties.LIT, pState.getValue(HEIGHT) - 1 != 0);
                pLevel.setBlockAndUpdate(pPos, newState);
            }
        }
        super.randomTick(pState, pLevel, pPos, pRandom);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(BlockStateProperties.LIT) && pState.getValue(HEIGHT) > 0) {
            switch (pState.getValue(CANDLES)) {
                case 1 -> {
                    addParticles(0, 0, 0, pState, pLevel, pPos);
                }
                case 2 -> {
                    addParticles(2 / 16D, -2 / 16D, -3 / 16D, pState, pLevel, pPos);
                    addParticles(-2 / 16D, 0, 3 / 16D, pState, pLevel, pPos);
                }
                case 3 -> {
                    addParticles(-3 / 16D, -1 / 16D, -4 / 16D, pState, pLevel, pPos);
                    addParticles(4 / 16D, -2 / 16D, -1 / 16D, pState, pLevel, pPos);
                    addParticles(-2 / 16D, 0, 4 / 16D, pState, pLevel, pPos);
                }
            }
        }
    }

    public void addParticles(double x, double y, double z, BlockState state, Level world, BlockPos pos) {
        double d = (double) pos.getX() + 0.5 + x;
        double e = (double) pos.getY() + PARTICLE_HEIGHT[state.getValue(HEIGHT)] + y;
        double f = (double) pos.getZ() + 0.5 + z;
        world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
        world.addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);
    }
}