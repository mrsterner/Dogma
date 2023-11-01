package dev.sterner.dogma.content.item.necro;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.registry.DogmaAttributeRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class AllBlackSwordItem extends SwordItem {

    private static final AttributeModifier REACH_MODIFIER =
            new AttributeModifier(UUID.fromString("5fc9d1ae-8e00-449b-be79-b8d31fa10eff"), "Weapon modifier", 1.5, AttributeModifier.Operation.ADDITION);


    public AllBlackSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create(super.getDefaultAttributeModifiers(pEquipmentSlot));
        if (pEquipmentSlot == EquipmentSlot.MAINHAND) {
            map.put(ForgeMod.ENTITY_REACH.get(), REACH_MODIFIER);
        }
        return map;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (!pStack.hasTag()) {
            CompoundTag tag = pStack.getOrCreateTag();
            tag.putInt(Constants.Nbt.ALL_BLACK, 0);
            pStack.setTag(tag);
        }
    }
}
