package dev.sterner.dogma.data;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.content.block.abyss.FleshBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;
import team.lodestar.lodestone.systems.datagen.statesmith.BlockStateSmith;

public class DogmaBlockStateSmithTypes {
    public static BlockStateSmith<Block> VARIABLE_BLOCK = new BlockStateSmith<>(Block.class, (block, provider) -> {
        String name = provider.getBlockName(block);

        ModelFile var0 = provider.models().cubeAll(name, Dogma.id("block/" + name));
        ModelFile var1 = provider.models().cubeAll(name + "_1", Dogma.id("block/" + name + "_1"));
        ModelFile var2 = provider.models().cubeAll(name + "_2", Dogma.id("block/" + name + "_2"));

        provider.getVariantBuilder(block)
                .partialState().with(FleshBlock.VARIANTS, 1).modelForState().modelFile(var0).addModel()
                .partialState().with(FleshBlock.VARIANTS, 2).modelForState().modelFile(var1).addModel()
                .partialState().with(FleshBlock.VARIANTS, 3).modelForState().modelFile(var2).addModel();

    });
}
