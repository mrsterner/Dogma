package dev.sterner.dogma.content.item.necro;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class EmeraldTabletItem extends BlockItem {
    public EmeraldTabletItem(Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player playerEntity = pContext.getPlayer();
        if (playerEntity != null && playerEntity.isShiftKeyDown() && !BookOfTheDeadItem.cancelPlacement(pContext.getLevel(), playerEntity)) {
            return super.useOn(pContext);
        }
        return InteractionResult.PASS;
    }
}