package dev.sterner.dogma;

import com.mojang.logging.LogUtils;
import dev.sterner.dogma.data.*;
import dev.sterner.dogma.foundation.registry.DogmaCreativeTabRegistry;
import dev.sterner.dogma.foundation.registry.DogmaRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

import static dev.sterner.dogma.foundation.registry.DogmaAttributeRegistry.ATTRIBUTES;
import static dev.sterner.dogma.foundation.registry.DogmaBlockEntityTypeRegistry.BLOCK_ENTITY_TYPES;
import static dev.sterner.dogma.foundation.registry.DogmaBlockRegistry.BLOCKS;
import static dev.sterner.dogma.foundation.registry.DogmaCreativeTabRegistry.CREATIVE_MODE_TABS;
import static dev.sterner.dogma.foundation.registry.DogmaEnchantmentRegistry.ENCHANTMENTS;
import static dev.sterner.dogma.foundation.registry.DogmaEntityTypeRegistry.ENTITY_TYPES;
import static dev.sterner.dogma.foundation.registry.DogmaItemRegistry.ITEMS;
import static dev.sterner.dogma.foundation.registry.DogmaMenuTypeRegistry.MENU_TYPES;
import static dev.sterner.dogma.foundation.registry.DogmaMobEffects.EFFECTS;
import static dev.sterner.dogma.foundation.registry.DogmaParticleTypeRegistry.PARTICLES;
import static dev.sterner.dogma.foundation.registry.DogmaRecipeRegistry.RECIPE_SERIALIZERS;
import static dev.sterner.dogma.foundation.registry.DogmaRecipeRegistry.RECIPE_TYPES;
import static dev.sterner.dogma.foundation.registry.DogmaSoundEventRegistry.SOUNDS;

@Mod(Dogma.MODID)
public class Dogma {
    public static final String MODID = "dogma";

    private static final Logger LOGGER = LogUtils.getLogger();

    public Dogma() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ENCHANTMENTS.register(modBus);
        BLOCKS.register(modBus);
        BLOCK_ENTITY_TYPES.register(modBus);
        ITEMS.register(modBus);
        ENTITY_TYPES.register(modBus);
        EFFECTS.register(modBus);
        PARTICLES.register(modBus);
        SOUNDS.register(modBus);
        MENU_TYPES.register(modBus);
        ATTRIBUTES.register(modBus);
        RECIPE_TYPES.register(modBus);
        RECIPE_SERIALIZERS.register(modBus);
        CREATIVE_MODE_TABS.register(modBus);

        DogmaRegistries.CURSE_DEFERRED_REGISTER.register(modBus);
        DogmaRegistries.KNOWLEDGE_DEFERRED_REGISTER.register(modBus);

        modBus.addListener(DogmaCreativeTabRegistry::populateItemGroups);
        modBus.addListener(DogmaDataGenerator::gatherData);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MODID, name);
    }
}
