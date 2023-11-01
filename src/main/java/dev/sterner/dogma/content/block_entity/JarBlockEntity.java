package dev.sterner.dogma.content.block_entity;

import dev.sterner.dogma.content.block.JarBlock;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public class JarBlockEntity extends LodestoneBlockEntity {
    public static final int MAX_LIQUID = 100;
    public static final int JAR_COLLECTION_RANGE = 10;
    public int liquidAmount = 0;

    public static final int EMPTY = 0;
    public static final int WATER = 1;
    public static final int BLOOD = 2;
    public static final int ACID = 3;
    public int liquidType = EMPTY;
    public boolean hasBrain = false;

    public JarBlockEntity(BlockPos pos, BlockState state) {
        super(BotDBlockEntityTypes.JAR, pos, state);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        boolean mark = false;
        if (world != null && !world.isClient) {
            if (world.getTime() % 10 == 0 && state.get(JarBlock.OPEN) && getBloodyCorpseAbove(world, pos)) {
                if (liquidAmount < MAX_LIQUID && (getLiquidType(BLOOD) || getLiquidType(EMPTY))) {
                    setLiquidType(BLOOD);
                    liquidAmount++;
                    setChanged();
                    mark = true;
                }
            }
        }
        if (mark) {
            setChanged();
        }
    }

    private boolean getBloodyCorpseAbove(World world, BlockPos pos) {
        for (double y = 0; y <= JAR_COLLECTION_RANGE; ++y) {
            BlockPos potentialCorpse = new BlockPos(pos.getX(), (int) (pos.getY() + y), pos.getZ());
            if (world.getBlockEntity(potentialCorpse) instanceof HookBlockEntity hookBlockEntity) {
                if (hookBlockEntity.hookedAge < Constants.Values.BLEEDING) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setLiquidType(int type) {
        this.liquidType = type;
        setChanged();
    }

    public boolean getLiquidType(int type) {
        return this.liquidType == type;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.liquidType = nbt.getByte(Constants.Nbt.LIQUID_TYPE);
        this.liquidAmount = nbt.getInt(Constants.Nbt.LIQUID_LEVEL);
        this.hasBrain = nbt.getBoolean(Constants.Nbt.BRAIN);
        markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt(Constants.Nbt.LIQUID_TYPE, liquidType);
        nbt.putInt(Constants.Nbt.LIQUID_LEVEL, this.liquidAmount);
        nbt.putBoolean(Constants.Nbt.BRAIN, this.hasBrain);
    }
}