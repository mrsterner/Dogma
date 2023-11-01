package dev.sterner.dogma.api;

import dev.sterner.dogma.foundation.registry.DogmaBlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BookBlockEntity extends BlockEntity {

    public BookBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(DogmaBlockEntityTypeRegistry.BOOK.get(), pPos, pBlockState);
    }
}
