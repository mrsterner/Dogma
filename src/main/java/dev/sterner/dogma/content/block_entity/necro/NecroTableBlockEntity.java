package dev.sterner.dogma.content.block_entity.necro;

import dev.sterner.dogma.api.PedestalInfo;
import dev.sterner.dogma.content.block_entity.PedestalBlockEntity;
import dev.sterner.dogma.foundation.necro.BasicNecrotableRitual;
import dev.sterner.dogma.foundation.recipe.necro.RitualRecipe;
import dev.sterner.dogma.foundation.registry.DogmaBlockEntityTypeRegistry;
import dev.sterner.dogma.foundation.registry.DogmaBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NecroTableBlockEntity extends LodestoneBlockEntity {
    public List<PedestalInfo> pedestalToActivate = new ArrayList<>();
    public List<LivingEntity> sacrificeCache = new ArrayList<>();

    public boolean isNecroTable = false;
    public boolean hasBotD = false;
    public boolean hasEmeraldTablet = false;
    public BlockPos ritualPos = null;
    public List<BlockPos> pedestalPosList = new ArrayList<>();
    public BasicNecrotableRitual currentBasicNecrotableRitual = null;
    public RitualRecipe ritualRecipe = null;
    public UUID userUuid = null;

    //Logic
    private boolean loaded = false;
    public int timer = -20;
    public boolean shouldRun = false;
    public int clientTime = 0;

    public NecroTableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public NecroTableBlockEntity(BlockPos pos, BlockState state) {
        this(DogmaBlockEntityTypeRegistry.NECRO.get(), pos, state);
    }

    public List<PedestalInfo> getPedestalInfo(Level world) {
        List<PedestalInfo> pairs = new ArrayList<>();
        for (BlockPos pos : pedestalPosList) {
            if (world.getBlockState(pos).is(DogmaBlockRegistry.PEDESTAL.get()) && world.getBlockEntity(pos) instanceof PedestalBlockEntity pedestalBlockEntity) {
                pairs.add(new PedestalInfo(pedestalBlockEntity.inventory.getStacks(), pos));
            }
        }
        return pairs;
    }
}
