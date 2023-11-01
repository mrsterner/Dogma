package dev.sterner.dogma.data;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.registry.DogmaBlockRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.systems.datagen.BlockStateSmithTypes;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneBlockStateProvider;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneItemModelProvider;
import team.lodestar.lodestone.systems.datagen.statesmith.AbstractBlockStateSmith;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static dev.sterner.dogma.foundation.registry.DogmaBlockRegistry.FLESH;
import static dev.sterner.dogma.foundation.registry.DogmaBlockRegistry.MARBLE;

public class DogmaBlockStateProvider extends LodestoneBlockStateProvider {
    public DogmaBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper, LodestoneItemModelProvider itemModelProvider) {
        super(output, Dogma.MODID, exFileHelper, itemModelProvider);
    }

    @Override
    public @NotNull String getName() {
        return "Mine in Abyss BlockStates";
    }

    @Override
    protected void registerStatesAndModels() {
        Set<Supplier<Block>> blocks = new HashSet<>(DogmaBlockRegistry.BLOCKS.getEntries());

        AbstractBlockStateSmith.StateSmithData data = new AbstractBlockStateSmith.StateSmithData(this, blocks::remove);

        BlockStateSmithTypes.FULL_BLOCK.act(data,
                MARBLE
        );

        DogmaBlockStateSmithTypes.VARIABLE_BLOCK.act(data, FLESH);
    }
}
