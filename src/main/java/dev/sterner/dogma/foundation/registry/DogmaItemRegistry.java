package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.api.AbstractSkullBlock;
import dev.sterner.dogma.api.MobEffectItem;
import dev.sterner.dogma.content.block.CandleBlock;
import dev.sterner.dogma.content.block.DebugWandItem;
import dev.sterner.dogma.content.block.SkullBlock;
import dev.sterner.dogma.content.block_entity.abyss.CurseWardingBoxBlockEntity;
import dev.sterner.dogma.content.item.abyss.LifeReverberatingStoneItem;
import dev.sterner.dogma.content.item.abyss.StarCompassItem;
import dev.sterner.dogma.content.item.necro.*;
import net.minecraft.core.Direction;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.systems.multiblock.MultiBlockItem;

public interface DogmaItemRegistry {
    DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Dogma.MODID);

    //start common
    RegistryObject<Item> DEBUG_WAND = ITEMS.register("debug_wand", () -> new DebugWandItem(settings()));
    RegistryObject<Item> MARBLE = ITEMS.register("marble", () -> new BlockItem(DogmaBlockRegistry.MARBLE.get(), settings()));
    //end common
    //------------------------------------------------
    //start abyss
    RegistryObject<StarCompassItem> STAR_COMPASS = ITEMS.register("star_compass", () -> new StarCompassItem(settings()));
    RegistryObject<Item> CRADLE_OF_DESIRE = ITEMS.register("cradle_of_desire", () -> new Item(settings()));
    RegistryObject<Item> THOUSAND_MEN_PINS = ITEMS.register("thousand_men_pins", () -> new Item(settings()));
    RegistryObject<Item> LIFE_REVERBERATING_STONE = ITEMS.register("life_reverberating_stone", () -> new LifeReverberatingStoneItem(settings()));

    RegistryObject<Item> BLOCK_OF_FLESH = ITEMS.register("block_of_flesh", () -> new BlockItem(DogmaBlockRegistry.FLESH.get(), settings()));

    RegistryObject<Item> CURSE_WARDING_BOX =
            ITEMS.register("curse_warding_box",
                    () -> new MultiBlockItem(
                            DogmaBlockRegistry.CURSE_WARDING_BOX.get(),
                            new Item.Properties(),
                            CurseWardingBoxBlockEntity.STRUCTURE)
            );
    //end abyss
    //------------------------------------------------
    //start necro
    RegistryObject<Item> BOOK_OF_THE_DEAD = ITEMS.register("book_of_the_dead", () -> new Item(settings()));

    RegistryObject<Item> PAPER_AND_QUILL = ITEMS.register("paper_and_quill", () -> new Item(settings().stacksTo(1)));
    RegistryObject<Item> CARPENTER_TOOLS = ITEMS.register("carpenter_tools", () -> new Item(settings().stacksTo(1).durability(32)));

    RegistryObject<Item> MEAT_CLEAVER = ITEMS.register("meat_cleaver", () -> new AxeItem(Tiers.NETHERITE, 6, -2, settings()));
    RegistryObject<Item> EMERALD_TABLET = ITEMS.register("emerald_tablet", () -> new EmeraldTabletItem(DogmaBlockRegistry.EMERALD_TABLET.get(), settings()));

    RegistryObject<Item> ALL_BLACK = ITEMS.register("all_black", () -> new AllBlackSwordItem(Tiers.NETHERITE, 8, -2, settings()));

    RegistryObject<Item> SYRINGE = ITEMS.register("syringe", () ->  new SyringeItem(settings()));


    RegistryObject<Item> SULFURIC_ACID = ITEMS.register("sulfuric_acid", () -> new Item(settings()));
    RegistryObject<Item> PHILOSOPHERS_STONE = ITEMS.register("philosophers_stone", () -> new Item(settings()));

    RegistryObject<Item> MORPHINE = ITEMS.register("morphine", () -> new MobEffectItem(settings(), DogmaMobEffects.MORPHINE.get()));
    RegistryObject<Item> EUTHANASIA = ITEMS.register("euthanasia", () -> new MobEffectItem(settings(), DogmaMobEffects.EUTHANASIA.get()));
    RegistryObject<Item> ADRENALINE = ITEMS.register("adrenaline", () -> new MobEffectItem(settings(), DogmaMobEffects.ADRENALINE.get()));
/*
    RegistryObject<Item> SKELETON_HELMET = ITEMS.register("skeleton_helmet", () -> new SkeletonArmorItem(ArmorItem.Type.HELMET, settings()));
    RegistryObject<Item> SKELETON_CHESTPLATE = ITEMS.register("skeleton_chestplate", () -> new SkeletonArmorItem(ArmorItem.Type.CHESTPLATE, settings()));
    RegistryObject<Item> SKELETON_LEGGINGS = ITEMS.register("skeleton_leggings", () -> new SkeletonArmorItem(ArmorItem.Type.LEGGINGS, settings()));
    RegistryObject<Item> SKELETON_BOOTS = ITEMS.register("skeleton_boots", () -> new SkeletonArmorItem(ArmorItem.Type.BOOTS, settings()));

    RegistryObject<Item> WITHER_HELMET = ITEMS.register("wither_helmet", () -> new WitherArmorItem(ArmorItem.Type.HELMET, settings()));
    RegistryObject<Item> WITHER_CHESTPLATE = ITEMS.register("wither_chestplate", () -> new WitherArmorItem(ArmorItem.Type.CHESTPLATE, settings()));
    RegistryObject<Item> WITHER_LEGGINGS = ITEMS.register("wither_leggings", () -> new WitherArmorItem(ArmorItem.Type.LEGGINGS, settings()));
    RegistryObject<Item> WITHER_BOOTS = ITEMS.register("wither_boots", () -> new WitherArmorItem(ArmorItem.Type.BOOTS, settings()));

 */

    RegistryObject<Item> SOAP = ITEMS.register("soap", () -> new SoapItem(settings()));
    RegistryObject<Item> QUARTZ_PEARL = ITEMS.register("quartz_pearl", () -> new Item(settings()));

    //RegistryObject<Item> CELLAR_KEY = ITEMS.register("cellar_key", () -> new CellarKeyItem(settings()));
    //RegistryObject<Item> CONTRACT = ITEMS.register("contract", () -> new ContractItem(settings()));
    RegistryObject<Item> PACKET = ITEMS.register("packet", () -> new Item(settings()));
    //RegistryObject<Item> CAGE = ITEMS.register("cage", () -> new CageItem(settings()));
    RegistryObject<Item> HOOK = ITEMS.register("hook", () -> new Item(settings()));
    RegistryObject<Item> METAL_HOOK = ITEMS.register("metal_hook", () -> new Item(settings()));
    //RegistryObject<Item> OLD_LETTER = ITEMS.register("old_letter", () -> new OldLetterItem(settings()));
    RegistryObject<Item> ROPE = ITEMS.register("rope", () -> new BlockItem(DogmaBlockRegistry.ROPE.get(), settings()));

    RegistryObject<Item> FLESH = ITEMS.register("flesh", () -> new Item(settings().food(Foods.PORKCHOP)));
    RegistryObject<Item> COOKED_FLESH = ITEMS.register("cooked_flesh", () -> new Item(settings().food(Foods.COOKED_PORKCHOP)));
    RegistryObject<Item> FAT = ITEMS.register("fat", () -> new Item(settings()));
    RegistryObject<Item> SKIN = ITEMS.register("skin", () -> new Item(settings()));
    RegistryObject<Item> EYE = ITEMS.register("eye", () -> new Item(settings()));
    RegistryObject<Item> BOTTLE_OF_BLOOD = ITEMS.register("bottle_of_blood", () -> new Item(settings()));

    RegistryObject<Item> VILLAGER_HEAD = ITEMS.register("villager_head", () -> new StandingAndWallBlockItem(DogmaBlockRegistry.VILLAGER_HEAD.get(), DogmaBlockRegistry.VILLAGER_WALL_HEAD.get(), settings(), Direction.DOWN));

    RegistryObject<Item> CANDLE = ITEMS.register("candle", () -> new StandingAndWallBlockItem(DogmaBlockRegistry.CANDLE.get(), DogmaBlockRegistry.CANDLE_WALL.get(), settings(), Direction.DOWN));

    RegistryObject<Item> BRAIN = ITEMS.register("brain", () -> new BlockItem(DogmaBlockRegistry.BRAIN.get(), settings()));
    RegistryObject<Item> HEART = ITEMS.register("heart", () -> new Item(settings()));

    //RegistryObject<Item> RETORT_FLASK = ITEMS.register("retort_flask", () -> new BlockItem(DogmaBlockRegistry.RETORT_FLASK_BLOCK, settings()));


    RegistryObject<Item> POPPY_POD = ITEMS.register("poppy_pod", () -> new Item(settings()));
    RegistryObject<Item> POPPY_SEEDS = ITEMS.register("poppy_seeds", () -> new ItemNameBlockItem(DogmaBlockRegistry.POPPY_CROP.get(), settings()));

    RegistryObject<Item> SULFUR = ITEMS.register("sulfur", () -> new Item(settings()));

    RegistryObject<Item> JAR = ITEMS.register("jar", () -> new BlockItem(DogmaBlockRegistry.JAR.get(), settings()));
    RegistryObject<Item> PEDESTAL = ITEMS.register("pedestal", () -> new BlockItem(DogmaBlockRegistry.PEDESTAL.get(), settings()));
    //RegistryObject<Item> REINFORCED_DOOR = ITEMS.register("reinforced_door", () -> new ReinforcedDoorBlock(, settings());
    //RegistryObject<Item> REINFORCED_BLOCK = ITEMS.register("reinforced_block", () -> new BlockItem());

    //end necro
    //------------------------------------------------

    private static Item.Properties settings() {
        return new Item.Properties();
    }

}
