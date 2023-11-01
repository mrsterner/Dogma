package dev.sterner.dogma.content.block_entity.necro;

import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public class BrainBlockEntity extends LodestoneBlockEntity {
    public BrainBlockEntity(BlockPos pos, BlockState state) {
        super(BotDBlockEntityTypes.BRAIN, pos, state);
    }
}