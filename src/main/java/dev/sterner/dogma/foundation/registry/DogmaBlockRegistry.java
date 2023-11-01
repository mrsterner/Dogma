package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.api.BookBlock;
import dev.sterner.dogma.content.block.abyss.FleshBlock;
import dev.sterner.dogma.content.block.abyss.curse_warding_box.CurseWardingBoxBlock;
import dev.sterner.dogma.content.block.abyss.curse_warding_box.CurseWardingBoxComponent;
import dev.sterner.dogma.content.block.necro.BookOfTheDeadBlock;
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

    Block EMERALD_TABLET = registerTablet("emerald_tablet", new TabletBlock(QuiltBlockSettings.of(Material.DECORATION)), settings());
    Block ROPE = register("rope", new RopeBlock(QuiltBlockSettings.of(Material.WOOL).strength(0.2F)), settings(), true);

    Block VILLAGER_WALL_HEAD = register("villager_wall_head", new BotDWallSkullBlock(BotDSkullBlock.Type.VILLAGER, QuiltBlockSettings.copyOf(Blocks.ZOMBIE_HEAD)), settings(), false);
    Block VILLAGER_HEAD = registerWallStanding("villager_head", new BotDSkullBlock(BotDSkullBlock.Type.VILLAGER, QuiltBlockSettings.copyOf(Blocks.ZOMBIE_HEAD)), VILLAGER_WALL_HEAD, settings(), true);

    Block CANDLE_WALL = register("candle_wall", new CandleWallBlock(QuiltBlockSettings.of(Material.DECORATION)), settings(), false);
    Block CANDLE = registerWallStanding("candle", new CandleBlock(QuiltBlockSettings.of(Material.DECORATION)), CANDLE_WALL, settings(), true);

    Block BRAIN = register("brain", new BrainBlock(QuiltBlockSettings.of(Material.SOLID_ORGANIC)), settings(), true);
    Block RETORT_FLASK_BLOCK = register("retort_flask_block", new RetortFlaskBlock(QuiltBlockSettings.of(Material.GLASS)), settings(), false);

    Block POPPY_CROP = register("poppy_crop", new PoppyCropBlock(QuiltBlockSettings.copy(Blocks.WHEAT)), settings(), false);

    Block SULFUR_PILE = register("sulfur_pile", new SulfurLayerBlock(QuiltBlockSettings.of(SULFUR_MATERIAL).requiresTool()), settings(), false);

    Block HOOK_BLOCK = register("hook_block", new HookBlock(QuiltBlockSettings.of(Material.WOOL).strength(0.2F), false), settings(), false);
    Block METAL_HOOK_BLOCK = register("metal_hook_block", new HookBlock(QuiltBlockSettings.of(Material.WOOL).strength(0.2F), true), settings(), false);
    Block JAR = register("jar", new JarBlock(QuiltBlockSettings.of(Material.GLASS).strength(0.3F).sounds(BlockSoundGroup.GLASS)), settings(), true);
    Block NECRO_TABLE = register("necro", new NecroTableBlock(QuiltBlockSettings.copy(Blocks.DEEPSLATE)), settings(), false);
    Block BUTCHER_TABLE = register("butcher", new ButcherBlock(QuiltBlockSettings.copy(Blocks.DARK_OAK_PLANKS)), settings(), false);
    Block PEDESTAL = register("pedestal", new PedestalBlock(QuiltBlockSettings.copy(Blocks.DEEPSLATE_BRICKS)), settings(), true);
    Block REINFORCED_DOOR = register("reinforced_door", new ReinforcedDoorBlock(QuiltBlockSettings.copyOf(Blocks.OAK_DOOR)), settings(), BotD.isDebugMode());
    Block REINFORCED_BLOCK = register("reinforced_block", new ReinforcedBlock(QuiltBlockSettings.copyOf(Blocks.REINFORCED_DEEPSLATE)), settings(), BotD.isDebugMode());

    //end necro
    //------------------------------------------------

}
