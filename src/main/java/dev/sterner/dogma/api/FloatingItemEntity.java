package dev.sterner.dogma.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FloatingItemEntity extends Entity {
    private static final TrackedData<ItemStack> DATA_ITEM_STACK = DataTracker.registerData(FloatingItemEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    public static final TrackedData<Boolean> IS_SPECIAL_RENDER = DataTracker.registerData(FloatingItemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public final float hoverStart;
    public ItemStack stack = ItemStack.EMPTY;

    public FloatingItemEntity(EntityType<?> type, Level world) {
        super(type, world);
        this.hoverStart = (float) (Math.random() * Math.PI * 2.0D);
        this.refreshPosition();
    }

    @Override
    public boolean collides() {
        return true;
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        if (!world.isClient()) {
            ItemStack handStack = player.getMainHandStack();
            if (handStack.isEmpty() && hand == Hand.MAIN_HAND) {
                player.setStackInHand(hand, getItem());
                this.remove(RemovalReason.DISCARDED);
                return ActionResult.CONSUME;
            }

        }
        return super.interactAt(player, hitPos, hand);
    }

    protected Item getDefaultItem() {
        return Items.AIR;
    }

    protected ItemStack getItemRaw() {
        return this.getDataTracker().get(DATA_ITEM_STACK);
    }

    public ItemStack getItem() {
        ItemStack itemstack = this.getItemRaw();
        return itemstack.isEmpty() ? this.getDefaultItem().getDefaultStack() : itemstack;
    }

    public void setItem(ItemStack stack) {
        if (!stack.isOf(this.getDefaultItem()) || stack.hasNbt()) {
            this.getDataTracker().set(DATA_ITEM_STACK, stack);
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(DATA_ITEM_STACK, ItemStack.EMPTY);
        this.getDataTracker().startTracking(IS_SPECIAL_RENDER, false);
    }

    @Override
    public void onTrackedDataUpdate(TrackedData<?> data) {
        if (DATA_ITEM_STACK.equals(data)) {
            stack = getDataTracker().get(DATA_ITEM_STACK);
        }
        super.onTrackedDataUpdate(data);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound tag) {
        ItemStack itemStack = this.getItemRaw();
        if (!itemStack.isEmpty()) {
            tag.put(Constants.Nbt.ITEM, itemStack.writeNbt(new NbtCompound()));
        }
        tag.putBoolean(Constants.Nbt.IS_SPECIAL, this.getDataTracker().get(IS_SPECIAL_RENDER));
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound tag) {
        ItemStack itemstack = ItemStack.fromNbt(tag.getCompound(Constants.Nbt.ITEM));
        this.setItem(itemstack);
        this.getDataTracker().set(IS_SPECIAL_RENDER, tag.getBoolean(Constants.Nbt.IS_SPECIAL));
    }

    public float getYOffset(float partialTicks) {
        return MathHelper.sin(((float) age + partialTicks) / 20.0F + hoverStart) * 0.1F + 0.1F;
    }

    public float getRotation(float partialTicks) {
        return ((float) age + partialTicks) / 20.0F + this.hoverStart;
    }
}
