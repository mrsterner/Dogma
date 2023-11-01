package dev.sterner.dogma.data;

import dev.sterner.dogma.foundation.registry.DogmaBlockRegistry;
import dev.sterner.dogma.foundation.registry.DogmaEntityTypeRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DogmaLootTableProvider extends LootTableProvider {

    public DogmaLootTableProvider(PackOutput pOutput) {
        super(pOutput, Set.of(), List.of(
                new SubProviderEntry(Blocks::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(Entities::new, LootContextParamSets.ENTITY)));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        super.validate(map, validationtracker);
    }

    public static class Blocks extends BlockLootSubProvider {
        protected Blocks() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        public void dropSelf(Supplier<? extends Block> block) {
            super.dropSelf(block.get());
        }

        @Override
        protected void generate() {
            DogmaBlockRegistry.BLOCKS.getEntries().forEach(blockRegistryObject -> dropSelf(blockRegistryObject.get()));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return DogmaBlockRegistry.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
        }
    }

    public static class Entities extends EntityLootSubProvider {

        public Entities() {
            super(FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        public void generate() {

        }

        @Override
        protected Stream<EntityType<?>> getKnownEntityTypes() {
            return DogmaEntityTypeRegistry.ENTITY_TYPES.getEntries().stream().map(Supplier::get);
        }
    }
}
