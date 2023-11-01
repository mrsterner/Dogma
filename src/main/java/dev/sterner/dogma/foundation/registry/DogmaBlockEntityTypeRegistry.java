package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.api.BookBlockEntity;
import dev.sterner.dogma.api.SkullBlockEntity;
import dev.sterner.dogma.content.block.necro.HookBlockEntity;
import dev.sterner.dogma.content.block_entity.PedestalBlockEntity;
import dev.sterner.dogma.content.block_entity.abyss.CurseWardingBoxBlockEntity;
import dev.sterner.dogma.content.block_entity.necro.BrainBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface DogmaBlockEntityTypeRegistry {
    DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Dogma.MODID);


    //start common
    RegistryObject<BlockEntityType<SkullBlockEntity>> SKULL =
            BLOCK_ENTITY_TYPES.register("skull",
                    () -> BlockEntityType.Builder.of(
                            SkullBlockEntity::new,
                            DogmaBlockRegistry.VILLAGER_HEAD.get(), DogmaBlockRegistry.VILLAGER_WALL_HEAD.get()
                    ).build(null)
            );

    RegistryObject<BlockEntityType<PedestalBlockEntity>> ITEM_PEDESTAL =
            BLOCK_ENTITY_TYPES.register("item_pedestal",
                    () -> BlockEntityType.Builder.of(
                            PedestalBlockEntity::new,
                            DogmaBlockRegistry.PEDESTAL.get()
                    ).build(null)
            );



    //end common
    //------------------------------------------------
    //start abyss
    RegistryObject<BlockEntityType<CurseWardingBoxBlockEntity>> CURSE_WARDING_BOX =
            BLOCK_ENTITY_TYPES.register("curse_warding_box",
                    () -> BlockEntityType.Builder.of(
                            CurseWardingBoxBlockEntity::new,
                            DogmaBlockRegistry.CURSE_WARDING_BOX.get()
                    ).build(null)
            );
    //end abyss
    //------------------------------------------------
    //start necro
    RegistryObject<BlockEntityType<BookBlockEntity>> BOOK =
            BLOCK_ENTITY_TYPES.register("book",
                    () -> BlockEntityType.Builder.of(
                            BookBlockEntity::new,
                            DogmaBlockRegistry.BOOK_OF_THE_DEAD.get()
                    ).build(null)
            );

    RegistryObject<BlockEntityType<BrainBlockEntity>> BRAIN =
            BLOCK_ENTITY_TYPES.register("brain",
                    () -> BlockEntityType.Builder.of(
                            BrainBlockEntity::new,
                            DogmaBlockRegistry.BRAIN.get()
                    ).build(null)
            );

    RegistryObject<BlockEntityType<HookBlockEntity>> HOOK =
            BLOCK_ENTITY_TYPES.register("hook",
                    () -> BlockEntityType.Builder.of(
                            HookBlockEntity::new,
                            DogmaBlockRegistry.HOOK_BLOCK.get(), DogmaBlockRegistry.METAL_HOOK_BLOCK.get()
                    ).build(null)
            );
    //end necro
    //------------------------------------------------

}
