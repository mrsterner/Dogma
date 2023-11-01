package dev.sterner.dogma.data;

import dev.sterner.dogma.Dogma;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Dogma.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DogmaDataGenerator {

    @SubscribeEvent
    public void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        DogmaItemModelProvider itemProvider = new DogmaItemModelProvider(output, helper);
        DogmaTagProvider.DogmaBlocks blockTags = new DogmaTagProvider.DogmaBlocks(output, provider, helper);

        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new DogmaTagProvider.DogmaItems(output, provider, blockTags.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new DogmaTagProvider.DogmaEntityTypes(output, provider, helper));
        generator.addProvider(event.includeServer(), new DogmaLootTableProvider(output));

        generator.addProvider(event.includeClient(), new DogmaBlockStateProvider(output, helper, itemProvider));
        generator.addProvider(event.includeClient(), itemProvider);
        generator.addProvider(event.includeClient(), new DogmaLanguageProvider(output));

    }
}
