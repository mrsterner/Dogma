package dev.sterner.dogma.content.block;

import dev.sterner.dogma.content.block_entity.PedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.lodestar.lodestone.systems.block.WaterLoggedEntityBlock;

public class PedestalBlock<T extends PedestalBlockEntity> extends WaterLoggedEntityBlock<T> {
    protected static final VoxelShape OUTLINE_SHAPE = Shapes.or(
            box(2D, 0D, 2D, 14D, 3D, 14D),
            box(4D, 3D, 4D, 12D, 11D, 12D),
            box(3D, 11D, 3D, 13D, 13D, 13D)
    );

    public PedestalBlock(Properties settings) {
        super(settings.noOcclusion());
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

}