package dev.sterner.dogma.api;

import dev.sterner.dogma.foundation.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FloatingItemEntity extends Entity {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(FloatingItemEntity.class, EntityDataSerializers.ITEM_STACK);
    public static final EntityDataAccessor<Boolean> IS_SPECIAL_RENDER = SynchedEntityData.defineId(FloatingItemEntity.class, EntityDataSerializers.BOOLEAN);
    public final float hoverStart;
    public ItemStack stack = ItemStack.EMPTY;

    public FloatingItemEntity(EntityType<?> type, Level world) {
        super(type, world);
        this.hoverStart = (float) (Math.random() * Math.PI * 2.0D);
        this.reapplyPosition();
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public InteractionResult interactAt(Player pPlayer, Vec3 pVec, InteractionHand pHand) {
        if (!level().isClientSide()) {
            ItemStack handStack = pPlayer.getMainHandItem();
            if (handStack.isEmpty() && pHand == InteractionHand.MAIN_HAND) {
                pPlayer.setItemInHand(pHand, getItem());
                this.remove(RemovalReason.DISCARDED);
                return InteractionResult.CONSUME;
            }

        }
        return super.interactAt(pPlayer, pVec, pHand);
    }


    protected Item getDefaultItem() {
        return Items.AIR;
    }

    protected ItemStack getItemRaw() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    public ItemStack getItem() {
        ItemStack itemstack = this.getItemRaw();
        return itemstack.isEmpty() ? this.getDefaultItem().getDefaultInstance() : itemstack;
    }

    public void setItem(ItemStack stack) {
        if (!stack.is(this.getDefaultItem()) || stack.hasTag()) {
            this.getEntityData().set(DATA_ITEM_STACK, stack);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
        this.getEntityData().define(IS_SPECIAL_RENDER, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (DATA_ITEM_STACK.equals(data)) {
            stack = getEntityData().get(DATA_ITEM_STACK);
        }
        super.onSyncedDataUpdated(data);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        ItemStack itemStack = this.getItemRaw();
        if (!itemStack.isEmpty()) {
            tag.put(Constants.Nbt.ITEM, itemStack.save(new CompoundTag()));
        }
        tag.putBoolean(Constants.Nbt.IS_SPECIAL, this.getEntityData().get(IS_SPECIAL_RENDER));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        ItemStack itemstack = ItemStack.of(tag.getCompound(Constants.Nbt.ITEM));
        this.setItem(itemstack);
        this.getEntityData().set(IS_SPECIAL_RENDER, tag.getBoolean(Constants.Nbt.IS_SPECIAL));
    }

    public float getYOffset(float partialTicks) {
        return Mth.sin(((float) tickCount + partialTicks) / 20.0F + hoverStart) * 0.1F + 0.1F;
    }

    public float getRotation(float partialTicks) {
        return ((float) tickCount + partialTicks) / 20.0F + this.hoverStart;
    }
}
