package dev.sterner.dogma.data;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.DogmaTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class DogmaTagProvider {
    public static class DogmaEntityTypes extends EntityTypeTagsProvider {

        public DogmaEntityTypes(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(pOutput, pProvider, Dogma.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            super.addTags(pProvider);
            this.tag(DogmaTags.CURSE_SUSCEPTIBLE)
                    .add(
                            EntityType.VILLAGER,
                            EntityType.ZOMBIE_VILLAGER,
                            EntityType.PLAYER,
                            EntityType.PIGLIN,
                            EntityType.PIGLIN_BRUTE,
                            EntityType.ZOMBIFIED_PIGLIN,
                            EntityType.WANDERING_TRADER
                    );
        }
    }

    public static class DogmaBlocks extends BlockTagsProvider {
        public DogmaBlocks(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Dogma.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {

        }
    }

    public static class DogmaItems extends ItemTagsProvider {
        public DogmaItems(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
            super(pOutput, pLookupProvider, pBlockTags, Dogma.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {

        }
    }
}
