package dev.sterner.dogma.content.item.necro;

import dev.sterner.dogma.foundation.Constants;
import dev.sterner.dogma.foundation.DogmaDamageSources;
import dev.sterner.dogma.foundation.registry.DogmaItemRegistry;
import dev.sterner.dogma.foundation.registry.DogmaMobEffects;
import dev.sterner.dogma.foundation.util.ComponentUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class SyringeItem extends Item {
    public SyringeItem(Properties pProperties) {
        super(pProperties);
    }

    private static final int MAX_USE_TIME = 32;

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        Player playerEntity = user instanceof Player ? (Player) user : null;
        if (playerEntity instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) playerEntity, stack);
        }
        boolean emptySyringe = false;

        if (!level.isClientSide && playerEntity != null) {
            MobEffectInstance instance = readStatusEffectNbt(stack);
            if (instance != null) {
                user.addEffect(new MobEffectInstance(instance));
                playerEntity.awardStat(Stats.ITEM_USED.get(this));
                if (!playerEntity.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                emptySyringe = true;
            } else if (stack.getOrCreateTag().contains(Constants.Nbt.BLOOD)) {
                CompoundTag nbt = stack.getTagElement(Constants.Nbt.BLOOD);
                if (nbt != null) {
                    UUID uuid = nbt.getUUID(Constants.Nbt.UUID);
                    if (uuid != playerEntity.getUUID()) {
                        playerEntity.addEffect(new MobEffectInstance(DogmaMobEffects.SANGUINE.get(), 20 * 20));
                    }
                }

                emptySyringe = true;
            } else {
                CompoundTag nbt = new CompoundTag();
                nbt.putString(Constants.Nbt.NAME, playerEntity.getScoreboardName());
                nbt.putUUID(Constants.Nbt.UUID, playerEntity.getUUID());
                stack.getOrCreateTag().put(Constants.Nbt.BLOOD, nbt);
                playerEntity.hurt(DogmaDamageSources.create(level, DogmaDamageSources.SANGUINE), 4f);
            }
            if (emptySyringe) {
                if (!playerEntity.getAbilities().instabuild) {
                    if (stack.isEmpty()) {
                        return DogmaItemRegistry.SYRINGE.get().getDefaultInstance();
                    }

                    playerEntity.getInventory().add(DogmaItemRegistry.SYRINGE.get().getDefaultInstance());
                }
            }
        }
        return stack;
    }

    @Nullable
    public static MobEffectInstance readStatusEffectNbt(ItemStack stack) {
        if (stack.hasTag() && stack.getOrCreateTag().contains(Constants.Nbt.STATUS_EFFECT_INSTANCE)) {
            CompoundTag nbt = stack.getTagElement(Constants.Nbt.STATUS_EFFECT_INSTANCE);
            if (nbt != null) {
                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.tryParse(nbt.getString(Constants.Nbt.STATUS_EFFECT)));
                if (effect != null) {
                    return new MobEffectInstance(effect, nbt.getInt(Constants.Nbt.DURATION), nbt.getInt(Constants.Nbt.AMPLIFIER));
                }
            }
        }
        return null;
    }

    public static void writeStatusEffectNbt(ItemStack stack, MobEffectInstance instance) {
        CompoundTag nbt = new CompoundTag();
        ResourceLocation identifier = ForgeRegistries.MOB_EFFECTS.getKey(instance.getEffect());
        if (identifier != null) {
            nbt.putString(Constants.Nbt.STATUS_EFFECT, identifier.toString());
            nbt.putInt(Constants.Nbt.DURATION, instance.getDuration());
            nbt.putInt(Constants.Nbt.AMPLIFIER, instance.getAmplifier());
            stack.getOrCreateTag().put(Constants.Nbt.STATUS_EFFECT_INSTANCE, nbt);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return MAX_USE_TIME;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @org.jetbrains.annotations.Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.getOrCreateTag().contains(Constants.Nbt.BLOOD)) {
            CompoundTag nbt = pStack.getTagElement(Constants.Nbt.BLOOD);
            if (nbt != null) {
                String name = nbt.getString(Constants.Nbt.NAME);
                String formattedName = ComponentUtils.capitalizeString(name);
                pTooltipComponents.add(Component.literal(formattedName).setStyle(Style.EMPTY.withColor(0xAC0014)));
            }
        }

        if (pStack.getOrCreateTag().contains(Constants.Nbt.STATUS_EFFECT_INSTANCE)) {
            MobEffectInstance instance = readStatusEffectNbt(pStack);
            if (instance != null) {
                MobEffect statusEffect = instance.getEffect();

                MutableComponent mutableText = Component.translatable(instance.getDescriptionId());
                if (instance.getAmplifier() > 0) {
                    mutableText = Component.translatable("potion.withAmplifier", mutableText, Component.translatable("potion.potency." + instance.getAmplifier()));
                }

                if (!instance.endsWithin(20)) {
                    mutableText = Component.translatable("potion.withDuration", mutableText, MobEffectUtil.formatDuration(instance, 1));
                }

                pTooltipComponents.add(mutableText.withStyle(statusEffect.getCategory().getTooltipFormatting()));
            }

        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }


}
