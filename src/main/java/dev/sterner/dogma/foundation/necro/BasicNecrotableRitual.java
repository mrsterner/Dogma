package dev.sterner.dogma.foundation.necro;

import dev.sterner.dogma.api.IRitual;
import dev.sterner.dogma.content.block_entity.necro.NecroTableBlockEntity;
import dev.sterner.dogma.foundation.handler.necro.RitualHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BasicNecrotableRitual implements IRitual {
    private final ResourceLocation id;
    public final RitualHandler ritualHandler = new RitualHandler();

    public BasicNecrotableRitual(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public void tick(Level world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
        if (ritualHandler.userUuid == null) {
            Player player = world.getNearestPlayer(blockPos.getZ(), blockPos.getY(), blockPos.getZ(), 16D, true);
            if (player != null) {
                ritualHandler.userUuid = player.getUUID();
            }
        }

        boolean sacrificesConsumed = ritualHandler.consumeSacrifices(world, blockPos, blockEntity);
        boolean itemsConsumed = ritualHandler.consumeItems(world, blockPos, blockEntity);
        if ((sacrificesConsumed && itemsConsumed) || ritualHandler.lockTick) {
            if (!ritualHandler.lockTick) {
                ritualHandler.runCommand(world, blockEntity, blockPos, "start");
                ritualHandler.generateStatusEffects(world, blockPos, blockEntity);
            }
            ritualHandler.lockTick = true;
            ritualHandler.runCommand(world, blockEntity, blockPos, "tick");
        }
    }

    @Override
    public void onStopped(Level world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
        if (ritualHandler.lockTick) {
            ritualHandler.runCommand(world, blockEntity, blockPos, "end");
            ritualHandler.summonSummons(world, blockPos, blockEntity);
            ritualHandler.summonItems(world, blockPos, blockEntity);
            this.reset();
        }
    }

    @Override
    public void reset() {
        ritualHandler.lockTick = false;
        ritualHandler.canCollectPedestals = true;
        ritualHandler.canCollectSacrifices = true;
    }
}
