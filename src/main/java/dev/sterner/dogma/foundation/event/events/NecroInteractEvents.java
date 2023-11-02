package dev.sterner.dogma.foundation.event.events;

import dev.sterner.dogma.content.block.RopeBlock;
import dev.sterner.dogma.content.block.necro.HookBlock;
import dev.sterner.dogma.foundation.capability.necro.NecroCorpseDataCapability;
import dev.sterner.dogma.foundation.capability.necro.NecroPlayerDataCapability;
import dev.sterner.dogma.foundation.capability.necro.NecroPlayerHaulerDataCapability;
import dev.sterner.dogma.foundation.registry.DogmaBlockRegistry;
import dev.sterner.dogma.foundation.registry.DogmaItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.Optional;

public class NecroInteractEvents {

    public static void init(PlayerInteractEvent.EntityInteractSpecific event){
        pickupCorpse(event);
    }

    public static void init(PlayerInteractEvent.RightClickBlock event) {
        createPedestal(event);
        placeHook(event);
        placeMetalHook(event);
        extendRope(event);
        placeCorpse(event);
    }

    private static void pickupCorpse(PlayerInteractEvent.EntityInteractSpecific event){
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        Level level = event.getLevel();
        if (entity instanceof Mob mobEntity) {
            NecroCorpseDataCapability capability = NecroCorpseDataCapability.getCapability(mobEntity);
            NecroPlayerHaulerDataCapability playerDataCapability = NecroPlayerHaulerDataCapability.getCapability(player);

            if (mobEntity.deathTime > 20 && capability.isCorpse) {
                if (!level.isClientSide() && player.isShiftKeyDown() && player.getMainHandItem().isEmpty()) {
                    if (playerDataCapability.getCorpseData().isEmpty()) {
                        BlockPos pos = mobEntity.getOnPos();
                        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1, 1);
                        playerDataCapability.setCorpseEntity(player, mobEntity);
                        mobEntity.remove(Entity.RemovalReason.DISCARDED);
                    }
                    //event.setCancellationResult(InteractionResult.CONSUME);
                }
            }
        }
        //event.setCancellationResult(InteractionResult.PASS);
    }

    private static void addParticle(Level world, BlockPos blockPos, BlockState blockState) {
        world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState),
                blockPos.getX() + ((double) world.random.nextFloat() - 0.5D),
                blockPos.getY() + 0.1D,
                blockPos.getZ() + ((double) world.random.nextFloat() - 0.5D),
                4.0D * ((double) world.random.nextFloat() - 0.5D),
                0.5D,
                ((double) world.random.nextFloat() - 0.5D) * 4.0D);
    }

    private static InteractionResult createPedestal(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        BlockPos blockPos = event.getPos();
        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND && player instanceof ServerPlayer serverPlayerEntity) {
            if (player.getMainHandItem().is(DogmaItemRegistry.CARPENTER_TOOLS.get())) {
                if (level.getBlockState(blockPos).is(Blocks.DEEPSLATE_TILE_WALL)) {
                    BlockState state = DogmaBlockRegistry.PEDESTAL.get().defaultBlockState();
                    player.getMainHandItem().hurt(1, level.random, serverPlayerEntity);
                    level.destroyBlock(blockPos, false);
                    addParticle(level, blockPos, state);

                    level.setBlockAndUpdate(blockPos, state);
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult placeHook(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        BlockPos blockPos = event.getPos();
        if (!level.isClientSide() && player.getMainHandItem().is(DogmaItemRegistry.HOOK.get()) && hand == InteractionHand.MAIN_HAND && level.getBlockState(blockPos).is(DogmaBlockRegistry.ROPE.get())) {
            if (level.getBlockState(blockPos).getValue(RopeBlock.ROPE) == RopeBlock.Rope.BOTTOM) {
                level.setBlockAndUpdate(blockPos, DogmaBlockRegistry.HOOK_BLOCK.get().defaultBlockState().setValue(HookBlock.FACING, player.getDirection()));
                if (!player.isCreative()) {
                    player.getMainHandItem().shrink(1);
                }
                return InteractionResult.CONSUME;
            }

        }
        return InteractionResult.PASS;
    }

    private static InteractionResult placeMetalHook(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        BlockPos blockPos = event.getPos();
        if (!level.isClientSide() && player.getMainHandItem().is(DogmaItemRegistry.METAL_HOOK.get()) && hand == InteractionHand.MAIN_HAND && level.getBlockState(blockPos).is(Blocks.CHAIN)) {
            if (level.getBlockState(blockPos.below()).is(Blocks.AIR)) {
                level.setBlockAndUpdate(blockPos, DogmaBlockRegistry.METAL_HOOK_BLOCK.get().defaultBlockState().setValue(HookBlock.FACING, player.getDirection()));
                if (!player.isCreative()) {
                    player.getMainHandItem().shrink(1);
                }
                return InteractionResult.CONSUME;
            }

        }
        return InteractionResult.PASS;
    }

    private static InteractionResult extendRope(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        BlockPos blockPos = event.getPos();
        if (!level.isClientSide() && player.getMainHandItem().is(DogmaItemRegistry.ROPE.get()) && hand == InteractionHand.MAIN_HAND && level.getBlockState(blockPos).is(DogmaBlockRegistry.ROPE.get())) {
            int y = blockPos.getY() - 1;
            while (level.getBlockState(new BlockPos(blockPos.getX(), y, blockPos.getZ())).is(DogmaBlockRegistry.ROPE.get())) {
                y--;
            }
            BlockPos targetPos = new BlockPos(blockPos.getX(), y, blockPos.getZ());
            if (level.getBlockState(targetPos).isAir()) {
                level.setBlockAndUpdate(targetPos, DogmaBlockRegistry.ROPE.get().defaultBlockState().setValue(RopeBlock.ROPE, RopeBlock.Rope.BOTTOM));
                if (!player.isCreative()) {
                    player.getMainHandItem().shrink(1);
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult placeCorpse(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        BlockPos blockPos = event.getPos();
        if (level instanceof ServerLevel serverWorld && hand == InteractionHand.MAIN_HAND && player.getMainHandItem().isEmpty() && player.isShiftKeyDown()) {
            NecroPlayerHaulerDataCapability capability = NecroPlayerHaulerDataCapability.getCapability(player);
            if (!capability.getCorpseData().isEmpty()) {
                CompoundTag nbtCompound = capability.getCorpseData();

                Optional<Entity> optionalEntity = EntityType.create(nbtCompound, level);
                if (optionalEntity.isPresent() && optionalEntity.get() instanceof LivingEntity storedLivingEntity) {
                    BlockPos pos = blockPos.relative(event.getFace());

                    storedLivingEntity.moveTo(pos, 0, 0);
                    storedLivingEntity.teleportTo(pos.getX(), pos.getY(), pos.getZ());
                    serverWorld.addFreshEntity(storedLivingEntity);

                    serverWorld.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1, 1);

                    //Reset Data
                    capability.clearCorpseData(player);
                    return InteractionResult.CONSUME;
                }

            }
        }
        return InteractionResult.PASS;
    }
}
