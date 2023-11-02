package dev.sterner.dogma.content.item.necro;

import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.registry.DogmaItemRegistry;
import dev.sterner.dogma.foundation.util.ComponentUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ContractItem extends Item {
    public ContractItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide() && pUsedHand == InteractionHand.MAIN_HAND) {
            ItemStack itemStack = pPlayer.getMainHandItem();
            CompoundTag nbt = new CompoundTag();
            nbt.putString(Constants.Nbt.NAME, pPlayer.getScoreboardName());
            nbt.putUUID(Constants.Nbt.UUID, pPlayer.getUUID());
            itemStack.getOrCreateTag().put(Constants.Nbt.CONTRACT, nbt);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag() && pStack.getOrCreateTag().contains(Constants.Nbt.CONTRACT)) {
            CompoundTag nbt = pStack.getTagElement(Constants.Nbt.CONTRACT);
            if (nbt != null) {
                String name = nbt.getString(Constants.Nbt.NAME);
                String formattedName = ComponentUtils.capitalizeString(name);
                pTooltipComponents.add(Component.literal(formattedName).setStyle(Style.EMPTY.withColor(0xAC0014)));
            }

        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Nullable
    public static LivingEntity getLivingFromContractNbt(Level world, ItemStack contract) {
        if (contract.is(DogmaItemRegistry.CONTRACT.get()) && contract.hasTag() && contract.getOrCreateTag().contains(Constants.Nbt.CONTRACT)) {
            CompoundTag nbt = contract.getTagElement(Constants.Nbt.CONTRACT);
            if (nbt != null) {
                int id = nbt.getInt(Constants.Nbt.ID);
                Entity entity = world.getEntity(id);
                if (entity instanceof LivingEntity living) {
                    return living;
                }
            }
        }
        return null;
    }

    public static int getIdFromContractNbt(ItemStack contract) {
        if (contract.is(DogmaItemRegistry.CONTRACT.get()) && contract.hasTag() && contract.getOrCreateTag().contains(Constants.Nbt.CONTRACT)) {
            CompoundTag nbt = contract.getTagElement(Constants.Nbt.CONTRACT);
            if (nbt != null) {
                return nbt.getInt(Constants.Nbt.ID);
            }
        }
        return 0;
    }
}
