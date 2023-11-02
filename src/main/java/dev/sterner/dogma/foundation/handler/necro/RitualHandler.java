package dev.sterner.dogma.foundation.handler.necro;

import com.mojang.brigadier.ParseResults;
import dev.sterner.dogma.api.CommandType;
import dev.sterner.dogma.api.FloatingItemEntity;
import dev.sterner.dogma.api.PedestalInfo;
import dev.sterner.dogma.content.block_entity.PedestalBlockEntity;
import dev.sterner.dogma.content.block_entity.necro.NecroTableBlockEntity;
import dev.sterner.dogma.content.item.necro.ContractItem;
import dev.sterner.dogma.foundation.capability.necro.NecroLivingEntityDataCapability;
import dev.sterner.dogma.foundation.registry.DogmaEntityTypeRegistry;
import dev.sterner.dogma.foundation.registry.DogmaItemRegistry;
import dev.sterner.dogma.foundation.registry.DogmaMobEffects;
import dev.sterner.dogma.foundation.registry.DogmaSoundEventRegistry;
import dev.sterner.dogma.foundation.util.DogmaUtils;
import dev.sterner.dogma.foundation.util.ParticleUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RitualHandler {
    public int pedestalTicker = 0;
    public int sacrificeTicker = -1;
    public boolean canCollectPedestals = true;
    public boolean canCollectSacrifices = true;
    public UUID userUuid = null;
    public boolean lockTick = false;
    public NonNullList<Integer> contract = NonNullList.withSize(8, 0);

    /**
     * Drops all resulting items at ritual origin
     *
     * @param level       level
     * @param blockPos    ritual origin
     * @param blockEntity ritualBlockEntity
     */
    public void summonItems(Level level, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
        double x = blockPos.getX() + 0.5;
        double y = blockPos.getY() + 0.5;
        double z = blockPos.getZ() + 0.5;

        if (blockEntity.currentBasicNecrotableRitual != null && blockEntity.ritualRecipe != null) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.playSound(null, x, y, z, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1F, 1F);
            }
            if (blockEntity.ritualRecipe.outputs != null) {
                if (blockEntity.ritualRecipe.outputs.size() > 1) {
                    for (ItemStack output : blockEntity.ritualRecipe.outputs) {
                        Containers.dropItemStack(level, x, y, z, output.copy());
                    }
                } else {
                    FloatingItemEntity itemEntity = new FloatingItemEntity(DogmaEntityTypeRegistry.FLOATING_ITEM_ENTITY.get(), level);
                    itemEntity.setItem(blockEntity.ritualRecipe.outputs.get(0).copy());
                    itemEntity.getEntityData().set(FloatingItemEntity.IS_SPECIAL_RENDER, blockEntity.ritualRecipe.isSpecial());
                    itemEntity.moveTo(blockPos.getX() + 0.5f, blockPos.getY() + 0.5f, blockPos.getZ() + 0.5f, 0, 0);
                    level.addFreshEntity(itemEntity);
                }
            }
        }
    }

    /**
     * Spawns all entities which was specified in the ritual recipe at ritual origin
     *
     * @param level       level
     * @param blockPos    ritual origin
     * @param blockEntity ritualBlockEntity
     */
    public void summonSummons(Level level, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
        if (blockEntity.ritualRecipe.summons != null) {
            for (EntityType<?> entityType : blockEntity.ritualRecipe.summons) {
                var entity = entityType.create(level);
                if (entity instanceof LivingEntity livingEntity) {
                    Vec3i levelPos = new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                    float angle = level.random.nextFloat() * 360;
                    double distance = level.random.nextDouble() * 2;
                    double x = levelPos.getX() + distance * Math.cos(Math.toRadians(angle));
                    double z = levelPos.getZ() + distance * Math.sin(Math.toRadians(angle));
                    livingEntity.moveTo(x, levelPos.getY(), z, level.random.nextFloat() * 360, 0);
                    level.addFreshEntity(livingEntity);
                }
            }
        }
    }

    /**
     * Adds all statusEffects to the user of the ritual
     *
     * @param level       level
     * @param blockPos    ritual origin
     * @param blockEntity ritualBlockEntity
     */
    public void generateStatusEffects(Level level, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
        int size = 16;
        List<LivingEntity> livingEntityList = new ArrayList<>();
        boolean foundContract = false;
        if (blockEntity.ritualRecipe.statusEffectInstance != null) {
            for (Integer id : this.contract) {
                if (id != 0) {
                    Entity entity = level.getEntity(id);
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntityList.add(livingEntity);
                        foundContract = true;
                    }
                }
            }
            if (!foundContract) {
                livingEntityList = level.getEntitiesOfClass(LivingEntity.class, new AABB(blockPos).inflate(size), Entity::isAlive);
            }
            for (LivingEntity living : livingEntityList) {
                for (MobEffectInstance instance : blockEntity.ritualRecipe.statusEffectInstance) {
                    if (living.canBeAffected(instance)) {
                        living.addEffect(new MobEffectInstance(instance));
                    }
                }
            }
        }
    }

    /**
     * Check if the pedestals have appropriate items matching recipe and consumes them in succession
     *
     * @param level       level
     * @param blockPos    ritual origin
     * @param blockEntity ritualBlockEntity
     * @return true if all items where consumed
     */
    public boolean consumeItems(Level level, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
        if (blockEntity.ritualRecipe == null) {
            return false;
        } else if (blockEntity.ritualRecipe.inputs != null && blockEntity.ritualRecipe.inputs.isEmpty()) {
            return true;
        }

        if (blockEntity.pedestalToActivate.isEmpty() && canCollectPedestals) {
            List<PedestalInfo> infoStream = blockEntity.getPedestalInfo(level).stream().filter(itemStackBlockPosPair -> !itemStackBlockPosPair.stacks().isEmpty()).toList();
            for (PedestalInfo info : infoStream) {
                activatePedestalIfMatchesRecipe(level, blockEntity, info);
            }

        } else if (!blockEntity.pedestalToActivate.isEmpty()) {
            pedestalTicker++;
            BlockPos particlePos = blockEntity.pedestalToActivate.get(0).pos();

            if (level instanceof ServerLevel serverLevel) {
                if (blockEntity.ritualRecipe.outputs != null) {
                    ParticleUtils.generateItemParticle(serverLevel, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, blockEntity.ritualRecipe.outputs);
                }
                ParticleUtils.spawnItemParticleBeam(new Vec3(particlePos.getX(), particlePos.getY(), particlePos.getZ()), new Vec3(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ()), serverLevel, blockEntity.pedestalToActivate.get(0).stacks().get(0));
            }

            if (pedestalTicker == 1) {
                level.playSound(null, blockEntity.pedestalToActivate.get(0).pos().getX(), blockEntity.pedestalToActivate.get(0).pos().getY(), blockEntity.pedestalToActivate.get(0).pos().getZ(), DogmaSoundEventRegistry.MISC_ITEM_BEAM.get(), SoundSource.BLOCKS, 0.5f, 0.75f * level.random.nextFloat() / 2);
            }

            if (pedestalTicker > 20 * 4) {
                if (level.getBlockEntity(blockEntity.pedestalToActivate.get(0).pos()) instanceof PedestalBlockEntity pedestalBlockEntity) {
                    pedestalBlockEntity.inventory.getStacks().get(0).shrink(1);
                    pedestalBlockEntity.setCrafting(false);
                    pedestalBlockEntity.setChanged();
                }
                blockEntity.pedestalToActivate.remove(0);
                blockEntity.setChanged();
                canCollectPedestals = false;
                pedestalTicker = 0;
                return blockEntity.pedestalToActivate.isEmpty();
            }
        }

        return false;
    }

    /**
     * If a pedestal have an item required for the ritual, add it to pedestal cache. Set it to crafting to lock its item
     *
     * @param level       level
     * @param blockEntity necrotable
     * @param info        info
     */
    private void activatePedestalIfMatchesRecipe(Level level, NecroTableBlockEntity blockEntity, PedestalInfo info) {
        for (Ingredient ingredient : Objects.requireNonNull(blockEntity.ritualRecipe.inputs)) {
            if (ingredient.test(info.stacks().get(0))) {
                BlockPos checkPos = info.pos();
                if (level.getBlockEntity(checkPos) instanceof PedestalBlockEntity pedestalBlockEntity) {
                    checkAndStoreContract(info);
                    pedestalBlockEntity.setCrafting(true);
                    pedestalBlockEntity.setChanged();
                    blockEntity.pedestalToActivate.add(info);
                }
            }
        }
    }

    /**
     * Check if the item is a contract and try to parse the contract to store its id
     *
     * @param info info
     */
    private void checkAndStoreContract(PedestalInfo info) {
        if (info.stacks().get(0).is(DogmaItemRegistry.CONTRACT.get()) && info.stacks().get(0).hasTag()) {
            for (int i = 0; i < contract.size(); i++) {
                int id = contract.get(i);
                if (id != 0) {
                    contract.set(i, ContractItem.getIdFromContractNbt(info.stacks().get(0)));
                    break;
                }
            }
        }
    }

    /**
     * Check and kill sacrifices around the ritual origin depending on recipe
     *
     * @param level       level
     * @param blockPos    origin
     * @param blockEntity ritualBlockEntity
     * @return true if sacrifice was successful
     */
    public boolean consumeSacrifices(Level level, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
        if (blockEntity.ritualRecipe.sacrifices == null) {
            return true;
        } else if (blockEntity.ritualRecipe.sacrifices.isEmpty()) {
            return true;
        }

        int size = 8;

        if (blockEntity.sacrificeCache.isEmpty() && canCollectSacrifices) {
            List<LivingEntity> livingEntityList = level.getEntitiesOfClass(LivingEntity.class, new AABB(blockPos).inflate(size), Entity::isAlive);
            List<EntityType<?>> ritualSacrifices = blockEntity.ritualRecipe.sacrifices;

            // Find and add the closest entity for each entity type to sacrificeCache
            for (EntityType<?> entityType : ritualSacrifices) {
                LivingEntity foundEntity = DogmaUtils.getClosestEntity(livingEntityList, entityType, blockPos);
                if (foundEntity != null) {
                    blockEntity.sacrificeCache.add(foundEntity);
                    livingEntityList.remove(foundEntity);
                }
            }
            return false;

        } else if (!blockEntity.sacrificeCache.isEmpty()) {
            sacrificeTicker++;
            if (sacrificeTicker >= 20 * 3) {
                if (blockEntity.sacrificeCache.get(0) instanceof Mob mob) {
                    NecroLivingEntityDataCapability capability = NecroLivingEntityDataCapability.getCapability(mob);
                    capability.setRitualPos(new Vec3(blockPos.getX() + 0.5f, blockPos.getY(), blockPos.getZ() + 0.5f));
                    mob.addEffect(new MobEffectInstance(DogmaMobEffects.SOUL_SIPHON.get(), 20 * 3));
                }
                canCollectSacrifices = false;
                blockEntity.sacrificeCache.remove(0);
                blockEntity.setChanged();
                sacrificeTicker = 0;
                return blockEntity.sacrificeCache.isEmpty();
            }
        }
        return blockEntity.sacrificeCache.isEmpty();
    }


    /**
     * Runs a command depending on which key phrase is used, "start", "tick", "end". Runs the {@link RitualHandler#runCommand(MinecraftServer, BlockPos, String)}
     *
     * @param level       level
     * @param blockEntity ritualBlockEntity
     * @param blockPos    pos of the ritual origin
     * @param phase       keyword for which phase the command should run in
     */
    public void runCommand(Level level, NecroTableBlockEntity blockEntity, BlockPos blockPos, String phase) {
        MinecraftServer minecraftServer = level.getServer();
        for (CommandType commandType : blockEntity.ritualRecipe.command) {
            if (commandType.type().equals(phase)) {
                runCommand(minecraftServer, blockPos, commandType.command());
            }
        }
    }

    /**
     * Runs the command with the command manager
     *
     * @param minecraftServer server
     * @param blockPos        ritual center
     * @param command         command to execute
     */
    private void runCommand(MinecraftServer minecraftServer, BlockPos blockPos, String command) {
        if (minecraftServer != null && !command.isEmpty()) {
            command = "execute positioned {pos} run " + command;
            String posString = blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ();
            String parsedCommand = command.replaceAll("\\{pos}", posString);
            CommandSourceStack commandSource = minecraftServer.createCommandSourceStack();
            Commands commandManager = minecraftServer.getCommands();
            ParseResults<CommandSourceStack> parseResults = commandManager.getDispatcher().parse(parsedCommand, commandSource);
            commandManager.performCommand(parseResults, parsedCommand);
        }
    }
}