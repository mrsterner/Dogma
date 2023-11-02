package dev.sterner.dogma.content.block;

import dev.sterner.dogma.content.block_entity.JarBlockEntity;
import dev.sterner.dogma.foundation.registry.DogmaBlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.block.WaterLoggedEntityBlock;

public class JarBlock<T extends JarBlockEntity> extends WaterLoggedEntityBlock<T> {
    protected static final VoxelShape OPEN_SHAPE = Shapes.or(
            box(4, 0, 4, 12, 10, 5),
            box(4, 0, 4, 5, 10, 12),
            box(4, 0, 11, 12, 10, 12),
            box(11, 0, 5, 12, 10, 12),

            box(4.5, 12, 4.5, 11.5, 14, 5.5),
            box(4.5, 12, 4.5, 5.5, 14, 11.5),
            box(4.5, 12, 10.5, 11.5, 14, 11.5),
            box(10.5, 12, 4.5, 11.5, 14, 11.5)
    );

    protected static final VoxelShape CLOSED_SHAPE = Shapes.or(box(5, 14, 5, 11, 16, 11));
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    public JarBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, false));
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof JarBlockEntity jarBlockEntity && jarBlockEntity.liquidAmount != 0) {
            if (!level.isClientSide) {
                ItemStack itemStack = new ItemStack(this);
                jarBlockEntity.saveToItem(itemStack);
                ItemEntity itemEntity = new ItemEntity(level, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, itemStack);
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        ItemStack itemStack = super.getCloneItemStack(world, pos, state);
        world.getBlockEntity(pos, DogmaBlockEntityTypeRegistry.JAR.get()).ifPresent((blockEntity) -> {
            blockEntity.saveToItem(itemStack);
        });
        return itemStack;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPEN);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pState.getValue(OPEN) ? OPEN_SHAPE : Shapes.or(CLOSED_SHAPE, OPEN_SHAPE);
    }
}