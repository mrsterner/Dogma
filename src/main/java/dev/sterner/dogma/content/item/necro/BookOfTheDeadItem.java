package dev.sterner.dogma.content.item.necro;

import dev.sterner.dogma.foundation.registry.DogmaBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class BookOfTheDeadItem extends BlockItem {
    public BookOfTheDeadItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player playerEntity = context.getPlayer();
        if (playerEntity != null && playerEntity.isShiftKeyDown() && !cancelPlacement(context.getLevel(), playerEntity)) {
            return super.useOn(context);
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide && !player.isShiftKeyDown()) {
            if (cancelPlacement(level, player)) {
                return InteractionResultHolder.success(player.getItemInHand(hand));
            }
            BookOfTheDeadScreen.openScreen(player);
            player.swing(hand);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    public static boolean cancelPlacement(Level world, Player player) {
        BlockHitResult blockHitResult = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = blockHitResult.getBlockPos();
            return world.getBlockState(pos).is(DogmaBlockRegistry.NECRO_TABLE);
        }
        return false;
    }
}
