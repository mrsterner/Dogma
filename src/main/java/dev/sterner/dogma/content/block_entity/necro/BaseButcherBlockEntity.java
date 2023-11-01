package dev.sterner.dogma.content.block_entity.necro;

import dev.sterner.dogma.api.HaulerBlockEntity;
import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.recipe.necro.ButcheringRecipe;
import dev.sterner.dogma.foundation.registry.DogmaBlockEntityTypeRegistry;
import dev.sterner.dogma.foundation.util.DogmaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

import java.util.Optional;

public class BaseButcherBlockEntity extends HaulerBlockEntity {
    public CompoundTag storedCorpseNbt = new CompoundTag();
    public LivingEntity storedLiving = null;
    public NonNullList<ItemStack> outputs = NonNullList.withSize(8, ItemStack.EMPTY);
    public NonNullList<Float> chances = NonNullList.withSize(8, 1F);
    public ButcheringRecipe butcheringRecipe = null;
    public boolean resetRecipe = true;

    public BaseButcherBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public InteractionResult onUse(Level world, BlockState state, BlockPos pos, Player player, InteractionHand hand, double probability, double particleOffset, boolean isNeighbour) {

    }


            private void dismemberAtRandom(Level world) {
        int i = world.getRandom().nextInt(3);
        switch (i) {
            case 0 -> setLArmVisible(false);
            case 1 -> setRArmVisible(false);
            case 2 -> setLLegVisible(false);
            case 3 -> setRLegVisible(false);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, outputs);
        DogmaUtils.writeChancesNbt(nbt, chances);
        if (!storedCorpseNbt.isEmpty()) {
            nbt.put(Constants.Nbt.CORPSE_ENTITY, getCorpseEntity());
        }
        nbt.putBoolean(Constants.Nbt.REFRESH, this.resetRecipe);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        ContainerHelper.loadAllItems(nbt, outputs);
        DogmaUtils.readChanceNbt(nbt, chances);
        setCorpse(nbt.getCompound(Constants.Nbt.CORPSE_ENTITY));
        this.resetRecipe = nbt.getBoolean(Constants.Nbt.REFRESH);
    }

    @Override
    public LivingEntity getCorpseLiving() {
        return storedLiving;
    }

    @Override
    public CompoundTag getCorpseEntity() {
        return storedCorpseNbt;
    }

    public void setCorpse(CompoundTag nbtCompound) {
        this.storedCorpseNbt = nbtCompound;
        if (!nbtCompound.isEmpty() && level != null) {
            Optional<Entity> living = EntityType.create(nbtCompound, level);
            if (living.isPresent() && living.get() instanceof LivingEntity livingEntity) {
                this.storedLiving = livingEntity;
            }
        }
    }

    @Override
    public void setCorpseEntity(LivingEntity entity) {
        CompoundTag nbtCompound = new CompoundTag();
        nbtCompound.putString("id", entity.getEncodeId());
        entity.save(nbtCompound);
        this.storedCorpseNbt = nbtCompound;
        this.storedLiving = entity;
    }

    @Override
    public void clearCorpseData() {
        this.storedCorpseNbt = new CompoundTag();
        this.storedLiving = null;
    }
}
