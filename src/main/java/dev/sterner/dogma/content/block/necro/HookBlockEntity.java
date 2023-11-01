package dev.sterner.dogma.content.block.necro;

import dev.sterner.dogma.content.block_entity.necro.BaseButcherBlockEntity;
import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.registry.DogmaBlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HookBlockEntity extends BaseButcherBlockEntity {
    public int hookedAge = 0;

    public HookBlockEntity(BlockPos pos, BlockState state) {
        super(DogmaBlockEntityTypeRegistry.HOOK.get(), pos, state);
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
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

    public InteractionResult onUse(Level world, BlockState state, BlockPos pos, Player player, InteractionHand hand, boolean isNeighbour) {
        return onUse(world, state, pos, player, hand, 0.25f, -1d, false);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt(Constants.Nbt.HOOKED_AGE, this.hookedAge);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.hookedAge = nbt.getInt(Constants.Nbt.HOOKED_AGE);
    }
}