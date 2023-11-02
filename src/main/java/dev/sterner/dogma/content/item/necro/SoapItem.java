package dev.sterner.dogma.content.item.necro;


import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

public class SoapItem extends Item {
    private static final int MAX_USE_TIME = 7200;

    public SoapItem(Properties settings) {
        super(settings.durability(8));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        var world = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        Player player = pContext.getPlayer();
        BlockState state = world.getBlockState(blockPos);

        return super.useOn(pContext);
    }


    @Override
    public int getUseDuration(ItemStack stack) {
        return MAX_USE_TIME;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }
}