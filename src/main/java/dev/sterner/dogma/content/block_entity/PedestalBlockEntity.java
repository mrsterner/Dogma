package dev.sterner.dogma.content.block_entity;

import dev.sterner.dogma.api.DogmaItemHolderBlockEntity;
import dev.sterner.dogma.foundation.registry.DogmaBlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public class PedestalBlockEntity extends DogmaItemHolderBlockEntity {

    public PedestalBlockEntity(BlockEntityType<? extends PedestalBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        this(DogmaBlockEntityTypeRegistry.ITEM_PEDESTAL.get(), pos, state);
    }
}