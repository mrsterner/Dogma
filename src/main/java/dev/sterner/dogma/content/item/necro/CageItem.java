package dev.sterner.dogma.content.item.necro;

import net.minecraft.world.item.Item;

public class CageItem extends Item {
    public CageItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!world.isClient()) {
            ItemStack stack = context.getStack();
            if (stack.hasNbt() && stack.getOrCreateNbt().contains(Constants.Nbt.STORED_ENTITY)) {
                NbtCompound entityCompound = stack.getOrCreateNbt().getCompound(Constants.Nbt.STORED_ENTITY);
                BlockPos pos = context.getBlockPos().offset(context.getSide());
                if (Registries.ENTITY_TYPE.get(new Identifier(entityCompound.getString("id"))).create((ServerWorld) world, null, null, pos, SpawnReason.SPAWN_EGG, true, false) instanceof MobEntity mob) {
                    mob.readNbt(entityCompound);
                    mob.setUuid(UUID.randomUUID());
                    mob.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
                    world.spawnEntity(mob);
                    world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5f, 1);
                    if (context.getPlayer() == null || !context.getPlayer().isCreative()) {
                        stack.removeSubNbt(Constants.Nbt.STORED_ENTITY);
                    }
                }
            }
            return ActionResult.success(world.isClient);
        }
        return super.useOnBlock(context);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack.hasNbt() && stack.getOrCreateNbt().contains(Constants.Nbt.STORED_ENTITY);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.hasNbt() && stack.getOrCreateNbt().contains(Constants.Nbt.STORED_ENTITY)) {
            Text name;
            NbtCompound entityCompound = stack.getNbt().getCompound(Constants.Nbt.STORED_ENTITY);
            if (entityCompound.contains("CustomName")) {
                name = Text.Serializer.fromJson(entityCompound.getString("CustomName"));
            } else {
                name = Registries.ENTITY_TYPE.get(new Identifier(entityCompound.getString("id"))).getName();
            }
            if (name != null) {
                tooltip.add(((MutableText) name).formatted(Formatting.GRAY));
            }
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
