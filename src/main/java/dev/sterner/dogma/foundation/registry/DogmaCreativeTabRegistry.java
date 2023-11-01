package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public interface DogmaCreativeTabRegistry {
    DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Dogma.MODID);

    //start common
    RegistryObject<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register(Dogma.MODID + ".main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + Dogma.MODID + "_main"))
                    .icon(() -> DogmaItemRegistry.MARBLE.get().getDefaultInstance())
                    .displayItems((parameters, output) -> output.accept(DogmaItemRegistry.MARBLE.get())).build()
    );
    //end common
    //------------------------------------------------
    //start abyss
    RegistryObject<CreativeModeTab> ABYSS = CREATIVE_MODE_TABS.register(Dogma.MODID + ".abyss",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + Dogma.MODID + "_abyss"))
                    .icon(() -> DogmaItemRegistry.STAR_COMPASS.get().getDefaultInstance())
                    .displayItems((parameters, output) -> output.accept(DogmaItemRegistry.STAR_COMPASS.get())).build()
    );
    //end abyss
    //------------------------------------------------
    //start necro
    RegistryObject<CreativeModeTab> NECRO = CREATIVE_MODE_TABS.register(Dogma.MODID + ".necro",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + Dogma.MODID + "_necro"))
                    .icon(() -> DogmaItemRegistry.BOOK_OF_THE_DEAD.get().getDefaultInstance())
                    .displayItems((parameters, output) -> output.accept(DogmaItemRegistry.BOOK_OF_THE_DEAD.get())).build()
    );
    //end necro
    //------------------------------------------------
    static void populateItemGroups(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == MAIN.getKey()) {

        }

        if (event.getTabKey() == ABYSS.getKey()) {

        }

        if (event.getTabKey() == NECRO.getKey()) {

        }
    }
}
