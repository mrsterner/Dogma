package dev.sterner.dogma.content.block_entity;

import dev.sterner.dogma.api.DogmaItemHolderBlockEntity;
import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.registry.DogmaBlockEntityTypeRegistry;
import dev.sterner.dogma.foundation.util.DogmaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public class PedestalBlockEntity extends DogmaItemHolderBlockEntity {
    private boolean crafting;
    public Vec3 ritualCenter;
    public int duration = 0;
    public double targetY = 0;
    private boolean loaded = false;

    public PedestalBlockEntity(BlockEntityType<? extends PedestalBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        this(DogmaBlockEntityTypeRegistry.ITEM_PEDESTAL.get(), pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        if (level != null) {
            if (!loaded) {
                setCrafting(false);
                setChanged();
                loaded = true;
            }
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.setCrafting(nbt.getBoolean(Constants.Nbt.CRAFTING));
        if (nbt.contains(Constants.Nbt.RITUAL_POS)) {
            this.ritualCenter = DogmaUtils.toVec3d(nbt.getCompound(Constants.Nbt.RITUAL_POS));
        }
        this.duration = nbt.getInt(Constants.Nbt.DURATION);
        this.targetY = nbt.getDouble(Constants.Nbt.TARGET_Y);
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean(Constants.Nbt.CRAFTING, this.isCrafting());
        if (hasRitualPos()) {
            nbt.put(Constants.Nbt.RITUAL_POS, DogmaUtils.fromVec3d(this.ritualCenter));
        }
        nbt.putInt(Constants.Nbt.DURATION, this.duration);
        nbt.putDouble(Constants.Nbt.TARGET_Y, this.targetY);
    }

    private boolean hasRitualPos() {
        return this.ritualCenter != null;
    }

    public boolean isCrafting() {
        return crafting;
    }

    public void setCrafting(boolean crafting) {
        this.crafting = crafting;
    }
}