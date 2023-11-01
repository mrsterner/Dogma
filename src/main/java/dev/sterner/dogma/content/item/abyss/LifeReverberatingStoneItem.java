package dev.sterner.dogma.content.item.abyss;

import dev.sterner.dogma.content.block_entity.abyss.CurseWardingBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class LifeReverberatingStoneItem extends Item {
    public LifeReverberatingStoneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
    }

    public List<CurseWardingBoxBlockEntity> getBox(Level pLevel, LivingEntity pEntityLiving) {
        List<CurseWardingBoxBlockEntity> list = new ArrayList<>();
        int range = 5;
        for (int x = -range; x < range; x++) {
            for (int y = -range; y < range; y++) {
                for (int z = -range; z < range; z++) {
                    BlockPos.MutableBlockPos pos = pEntityLiving.getOnPos().offset(x, y, z).mutable();
                    if (pLevel.getBlockEntity(pos) instanceof CurseWardingBoxBlockEntity cwbe) {
                        list.add(cwbe);
                    }
                }
            }
        }

        return list;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        List<CurseWardingBoxBlockEntity> blockEntities = getBox(pLevel, pEntityLiving);
        for (CurseWardingBoxBlockEntity be : blockEntities) {
            be.changeState();
        }

        return pStack;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 40;
    }


    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.TOOT_HORN;
    }
}