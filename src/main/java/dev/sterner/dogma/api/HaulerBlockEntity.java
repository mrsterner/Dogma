package dev.sterner.dogma.api;

import dev.sterner.dogma.foundation.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public abstract class HaulerBlockEntity extends LodestoneBlockEntity implements IHauler {

    public boolean headVisible = true;
    public boolean rArmVisible = true;
    public boolean lArmVisible = true;
    public boolean rLegVisible = true;
    public boolean lLegVisible = true;

    public HaulerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public boolean getHeadVisible() {
        return headVisible;
    }

    @Override
    public boolean getRArmVisible() {
        return rArmVisible;
    }

    @Override
    public boolean getLArmVisible() {
        return lArmVisible;
    }

    @Override
    public boolean getRLegVisible() {
        return rLegVisible;
    }

    @Override
    public boolean getLLegVisible() {
        return lLegVisible;
    }

    @Override
    public void setHeadVisible(boolean visible) {
        this.headVisible = visible;
    }

    @Override
    public void setRArmVisible(boolean visible) {
        this.rArmVisible = visible;
    }

    @Override
    public void setLArmVisible(boolean visible) {
        this.lArmVisible = visible;
    }

    @Override
    public void setRLegVisible(boolean visible) {
        this.rLegVisible = visible;
    }

    @Override
    public void setLLegVisible(boolean visible) {
        this.lLegVisible = visible;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean(Constants.Nbt.HEAD_VISIBLE, getHeadVisible());
        nbt.putBoolean(Constants.Nbt.RIGHT_ARM_VISIBLE, getRLegVisible());
        nbt.putBoolean(Constants.Nbt.LEFT_ARM_VISIBLE, getLArmVisible());
        nbt.putBoolean(Constants.Nbt.RIGHT_LEG_VISIBLE, getRLegVisible());
        nbt.putBoolean(Constants.Nbt.LEFT_LEG_VISIBLE, getLLegVisible());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        setHeadVisible(nbt.getBoolean(Constants.Nbt.HEAD_VISIBLE));
        setRArmVisible(nbt.getBoolean(Constants.Nbt.RIGHT_ARM_VISIBLE));
        setLArmVisible(nbt.getBoolean(Constants.Nbt.LEFT_ARM_VISIBLE));
        setRLegVisible(nbt.getBoolean(Constants.Nbt.RIGHT_LEG_VISIBLE));
        setLLegVisible(nbt.getBoolean(Constants.Nbt.LEFT_LEG_VISIBLE));
    }
}
