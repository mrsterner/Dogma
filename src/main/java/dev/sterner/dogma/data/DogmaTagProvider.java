package dev.sterner.dogma.data;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.Constants;
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

import static net.minecraft.world.entity.EntityType.*;

public class DogmaTagProvider {
    public static class DogmaEntityTypes extends EntityTypeTagsProvider {

        public DogmaEntityTypes(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(pOutput, pProvider, Dogma.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            super.addTags(pProvider);
            this.tag(Constants.Tags.CURSE_SUSCEPTIBLE)
                    .add(
                            VILLAGER,
                            ZOMBIE_VILLAGER,
                            PLAYER,
                            PIGLIN,
                            PIGLIN_BRUTE,
                            ZOMBIFIED_PIGLIN,
                            WANDERING_TRADER
                    );

            tag(Constants.Tags.BUTCHERABLE)
                    .add(COW)
                    .add(VILLAGER)
                    .add(PILLAGER)
                    .add(SHEEP)
                    .add(PIG)
                    .add(PIGLIN)
                    .add(PIGLIN_BRUTE)
                    .add(CHICKEN)
                    .add(PLAYER);

            tag(Constants.Tags.CAGEABLE_BLACKLIST)
                    .add(PLAYER)
                    .add(ENDER_DRAGON)
                    .add(ELDER_GUARDIAN)
                    .add(WITHER);

            tag(Constants.Tags.SOUL_WEAK)
                    .add(ALLAY)
                    .add(AXOLOTL)
                    .add(BAT)
                    .add(CAT)
                    .add(CHICKEN)
                    .add(COD)
                    .add(COW)
                    .add(FOX)
                    .add(FROG)
                    .add(GLOW_SQUID)
                    .add(MOOSHROOM)
                    .add(OCELOT)
                    .add(PARROT)
                    .add(PIG)
                    .add(PUFFERFISH)
                    .add(RABBIT)
                    .add(SALMON)
                    .add(SHEEP)
                    .add(SQUID)
                    .add(TADPOLE)
                    .add(TROPICAL_FISH)
                    .add(BEE)
                    .add(CAVE_SPIDER)
                    .add(SPIDER)
                    .add(WOLF)
                    .add(CREEPER)
                    .add(ENDERMITE)
                    .add(SHULKER)
                    .add(SILVERFISH)
                    .add(SLIME)
                    .add(MAGMA_CUBE)
                    .add(PHANTOM)
                    .add(VEX);

            tag(Constants.Tags.SOUL_REGULAR)
                    .add(DONKEY)
                    .add(HORSE)
                    .add(MULE)
                    .add(SKELETON_HORSE)
                    .add(SNOW_GOLEM)
                    .add(STRIDER)
                    .add(TURTLE)
                    .add(DOLPHIN)
                    .add(GOAT)
                    .add(IRON_GOLEM)
                    .add(LLAMA)
                    .add(PANDA)
                    .add(POLAR_BEAR)
                    .add(TRADER_LLAMA)
                    .add(ZOMBIFIED_PIGLIN)
                    .add(BLAZE)
                    .add(DROWNED)
                    .add(ELDER_GUARDIAN)
                    .add(RAVAGER)
                    .add(SKELETON)
                    .add(HUSK)
                    .add(GHAST)
                    .add(GUARDIAN)
                    .add(HOGLIN)
                    .add(STRAY)
                    .add(WITHER_SKELETON)
                    .add(ZOGLIN)
                    .add(ZOMBIE)
                    .add(CAMEL);

            tag(Constants.Tags.SOUL_STRONG)
                    .add(VILLAGER)
                    .add(WANDERING_TRADER)
                    .add(ENDERMAN)
                    .add(PIGLIN)
                    .add(EVOKER)
                    .add(PIGLIN_BRUTE)
                    .add(VINDICATOR)
                    .add(WARDEN)
                    .add(WITCH)
                    .add(ZOMBIE_VILLAGER)
                    .add(SNIFFER)
                    .add(ENDER_DRAGON)
                    .add(WITHER)

            ;
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
