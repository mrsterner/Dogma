package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.api.BookBlock;
import dev.sterner.dogma.content.block.*;
import dev.sterner.dogma.content.block.abyss.FleshBlock;
import dev.sterner.dogma.content.block.abyss.curse_warding_box.CurseWardingBoxBlock;
import dev.sterner.dogma.content.block.abyss.curse_warding_box.CurseWardingBoxComponent;
import dev.sterner.dogma.content.block.necro.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface DogmaBlockRegistry {
    DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Dogma.MODID);

//start common

    //end common
    //------------------------------------------------
    //start abyss
    RegistryObject<Block> MARBLE = BLOCKS.register("marble", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    RegistryObject<Block> FLESH = BLOCKS.register("flesh", () -> new FleshBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NETHER)));

    RegistryObject<Block> CURSE_WARDING_BOX =
            BLOCKS.register("curse_warding_box",
                    () -> new CurseWardingBoxBlock<>(
                            BlockBehaviour.Properties.of()
                                    .noOcclusion()
                    ).setBlockEntity(DogmaBlockEntityTypeRegistry.CURSE_WARDING_BOX)
            );
    RegistryObject<Block> CURSE_WARDING_BOX_COMPONENT =
            BLOCKS.register("curse_warding_box_component",
                    () -> new CurseWardingBoxComponent(
                            BlockBehaviour.Properties.copy(CURSE_WARDING_BOX.get())
                                    .lootFrom(CURSE_WARDING_BOX)
                                    .noOcclusion()
                    )
            );
    //end abyss
    //------------------------------------------------
    //start necro
    RegistryObject<Block> BOOK_OF_THE_DEAD = BLOCKS.register("book_of_the_dead", () -> new BookOfTheDeadBlock(BlockBehaviour.Properties.of()));

    RegistryObject<Block> EMERALD_TABLET = BLOCKS.register("emerald_tablet", () -> new TabletBlock(BlockBehaviour.Properties.of()));
    RegistryObject<Block> ROPE = BLOCKS.register("rope", () -> new RopeBlock(BlockBehaviour.Properties.of()));

    RegistryObject<Block> VILLAGER_WALL_HEAD = BLOCKS.register("villager_wall_head", () -> new WallSkullBlock(SkullBlock.Type.VILLAGER, BlockBehaviour.Properties.of()));
    RegistryObject<Block> VILLAGER_HEAD = BLOCKS.register("villager_head", () -> new SkullBlock(SkullBlock.Type.VILLAGER, BlockBehaviour.Properties.of()));

    RegistryObject<Block> CANDLE_WALL = BLOCKS.register("candle_wall", () -> new CandleWallBlock(BlockBehaviour.Properties.of()));
    RegistryObject<Block> CANDLE = BLOCKS.register("candle", () -> new CandleBlock(BlockBehaviour.Properties.of()));

    RegistryObject<Block> BRAIN = BLOCKS.register("brain", () -> new BrainBlock(BlockBehaviour.Properties.of()));

    RegistryObject<Block> POPPY_CROP = BLOCKS.register("poppy_crop", () -> new PoppyCropBlock(BlockBehaviour.Properties.of()));

    RegistryObject<Block> SULFUR_PILE = BLOCKS.register("sulfur_pile", () -> new SulfurLayerBlock(BlockBehaviour.Properties.of()));

    RegistryObject<Block> HOOK_BLOCK = BLOCKS.register("hook_block", () -> new HookBlock(BlockBehaviour.Properties.of(), false));
    RegistryObject<Block> METAL_HOOK_BLOCK = BLOCKS.register("metal_hook_block", () -> new HookBlock(BlockBehaviour.Properties.of(), true));
    RegistryObject<Block> JAR = BLOCKS.register("jar", () -> new JarBlock<>(BlockBehaviour.Properties.of()).setBlockEntity(DogmaBlockEntityTypeRegistry.JAR));
    RegistryObject<Block> NECRO_TABLE = BLOCKS.register("necro", () -> new NecroTableBlock(BlockBehaviour.Properties.of()));
    RegistryObject<Block> BUTCHER_TABLE = BLOCKS.register("butcher", () -> new ButcherBlock(BlockBehaviour.Properties.of()));
    RegistryObject<Block> PEDESTAL = BLOCKS.register("pedestal", () -> new PedestalBlock<>(BlockBehaviour.Properties.of()).setBlockEntity(DogmaBlockEntityTypeRegistry.ITEM_PEDESTAL));


    //end necro
    //------------------------------------------------

}
