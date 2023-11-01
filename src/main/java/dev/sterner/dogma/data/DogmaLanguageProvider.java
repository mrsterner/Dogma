package dev.sterner.dogma.data;

import dev.sterner.dogma.Dogma;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.helpers.DataHelper;

import java.util.HashSet;
import java.util.Set;

import static dev.sterner.dogma.foundation.registry.DogmaBlockRegistry.BLOCKS;
import static dev.sterner.dogma.foundation.registry.DogmaItemRegistry.ITEMS;

public class DogmaLanguageProvider extends LanguageProvider {
    public DogmaLanguageProvider(PackOutput output) {
        super(output, Dogma.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        Set<RegistryObject<Block>> blocks = new HashSet<>(BLOCKS.getEntries());
        Set<RegistryObject<Item>> items = new HashSet<>(ITEMS.getEntries());

        add("itemGroup.mineinabyss.main", "Mine in Abyss");

        blocks.forEach(b -> {
            String name = b.get().getDescriptionId().replaceFirst("block\\.mineinabyss\\.", "");
            name = makeProper(DataHelper.toTitleCase(name, "_"));
            add(b.get().getDescriptionId(), name);
        });

        DataHelper.takeAll(items, i -> i.get() instanceof BlockItem && !(i.get() instanceof ItemNameBlockItem));
        items.forEach(i -> {
            String name = i.get().getDescriptionId().replaceFirst("item\\.mineinabyss\\.", "");
            name = makeProper(DataHelper.toTitleCase(name, "_"));
            add(i.get().getDescriptionId(), name);
        });
    }

    public String makeProper(String s) {
        s = s
                .replaceAll("Of", "of")
                .replaceAll("The", "the")
                // Temp
                .replaceAll("Soul Stained", "Soulstained")
                .replaceAll("Soul Hunter", "Soulhunter");
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
