package dev.sterner.dogma.data;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.content.item.necro.BookOfTheDeadItem;
import dev.sterner.dogma.foundation.registry.DogmaItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import team.lodestar.lodestone.systems.datagen.ItemModelSmithTypes;
import team.lodestar.lodestone.systems.datagen.itemsmith.AbstractItemModelSmith;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneItemModelProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class DogmaItemModelProvider extends LodestoneItemModelProvider {
    public DogmaItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Dogma.MODID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Dogma Item Models";
    }

    @Override
    protected void registerModels() {
        Set<Supplier<Item>> items = new HashSet<>(DogmaItemRegistry.ITEMS.getEntries());

        items.removeIf(i -> i.get() instanceof BlockItem && !(i.get() instanceof BookOfTheDeadItem));

        AbstractItemModelSmith.ItemModelSmithData data = new AbstractItemModelSmith.ItemModelSmithData(this, items::remove);


        //Everything which is left in "items" will get generated
        ItemModelSmithTypes.GENERATED_ITEM.act(data, items);
    }
}
