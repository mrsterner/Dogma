package dev.sterner.dogma.content.block.necro;

public class HookBlockEntity extends BaseButcherBlockEntity {
    public int hookedAge = 0;

    public HookBlockEntity(BlockPos pos, BlockState state) {
        super(BotDBlockEntityTypes.HOOK, pos, state);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        boolean mark = false;

        if (world != null && !world.isClient) {
            if (world.getTime() % 20 == 0 && !storedCorpseNbt.isEmpty()) {
                mark = true;
                if (hookedAge < Constants.Values.BLEEDING) {
                    hookedAge++;
                } else {
                    hookedAge = Constants.Values.BLEEDING;
                }
            }
            if (storedCorpseNbt.isEmpty()) {
                hookedAge = 0;
            }
        }
        if (mark) {
            markDirty();
        }
    }

    public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand, boolean isNeighbour) {
        return onUse(world, state, pos, player, hand, 0.25f, -1d, false);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt(Constants.Nbt.HOOKED_AGE, this.hookedAge);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.hookedAge = nbt.getInt(Constants.Nbt.HOOKED_AGE);
    }
}