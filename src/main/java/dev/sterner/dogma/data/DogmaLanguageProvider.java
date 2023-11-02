package dev.sterner.dogma.data;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.registry.DogmaEnchantmentRegistry;
import dev.sterner.dogma.foundation.registry.DogmaItemRegistry;
import dev.sterner.dogma.foundation.registry.DogmaMobEffects;
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

        add("itemGroup.dogma_main", "Dogma");
        add("itemGroup.dogma_abyss", "Abyss");
        add("itemGroup.dogma_necro", "Necromancy");

        blocks.forEach(b -> {
            String name = b.get().getDescriptionId().replaceFirst("block\\.dogma\\.", "");
            name = makeProper(DataHelper.toTitleCase(name, "_"));
            add(b.get().getDescriptionId(), name);
        });

        DataHelper.takeAll(items, i -> i.get() instanceof BlockItem && !(i.get() instanceof ItemNameBlockItem));
        items.forEach(i -> {
            String name = i.get().getDescriptionId().replaceFirst("item\\.dogma\\.", "");
            name = makeProper(DataHelper.toTitleCase(name, "_"));
            add(i.get().getDescriptionId(), name);
        });


        add("death.attack.dogma.sacrifice", "%1$s was sacrificed");
        add("death.attack.dogma.sanguine", "%1$s was drained");
        add(DogmaMobEffects.SOUL_SIPHON.get(), "Soul Siphon");
        add(DogmaMobEffects.SOUL_SICKNESS.get(), "Soul Sickness");
        add(DogmaEnchantmentRegistry.BUTCHERING.get(), "Butchering");

        add("rei.book_of_the_dead.butchering_drops", "Butchering Drops");
        add("emi.category.book_of_the_dead.butchering", "Butchering Drops");
        add("tooltip.book_of_the_dead.old_friend", "Letter to an old friend");
        add("tooltip.book_of_the_dead.from_archive", "From Library Archive");
        add("info.book_of_the_dead.door_locked", "Door Locked");

        add("book_of_the_dead.book_of_the_dead.subtitle", "Death of the book");
        add("book_of_the_dead.book_of_the_dead", "Book of the Dead");
        add("book_of_the_dead.book_of_the_dead:book_of_the_dead", "Book of the Dead");
        add("book_of_the_dead.book_of_the_dead.landing", "This is a book of dead things and stuff");

        add(DogmaMobEffects.MORPHINE.get(), "Morphine");
        add(DogmaMobEffects.ADRENALINE.get(), "Adrenaline");
        add(DogmaMobEffects.EUTHANASIA.get(), "Euthanasia");
        add(DogmaMobEffects.SANGUINE.get(), "Sanguine Infection");

        add("book_of_the_dead.gui.book.page.text.empty", "");

        add("book_of_the_dead.gui.book.page.headline.main", "Book of the Dead");
        add("book_of_the_dead.gui.book.page.headline.glossary", "Glossary");
        add("book_of_the_dead.gui.book.page.headline.knowledge", "Knowledge");
        add("book_of_the_dead.gui.book.page.headline.knowledge.2", "Knowledge");

        add("book_of_the_dead.gui.book.page.headline.butcher", "Butchering");
        add("book_of_the_dead.gui.book.page.headline.butcher.1", "Butchering");
        add("book_of_the_dead.gui.book.page.headline.butcher.2", "Butchering");
        add("book_of_the_dead.gui.book.page.headline.butcher.3", "Butchering");

        add("book_of_the_dead.gui.book.page.headline.necro", "Necromancy");
        add("book_of_the_dead.gui.book.page.headline.necro.1", "Necromancy");
        add("book_of_the_dead.gui.book.page.headline.necro.2", "Necromancy");
        add("book_of_the_dead.gui.book.page.headline.necro.3", "Necromancy");

        add("book_of_the_dead.gui.book.page.headline.tools", "Tools");

        add("book_of_the_dead.gui.book.page.text.main.1", "Welcome");
        add("book_of_the_dead.gui.book.page.text.main.2", "Welcome");
        add("book_of_the_dead.gui.book.page.text.main.3", "Welcome");

        add("book_of_the_dead.gui.book.page.text.necro.1", "Welcome Necromancer");
        add("book_of_the_dead.gui.book.page.text.necro.2", "Welcome Necromancer");
        add("book_of_the_dead.gui.book.page.text.necro.3", "Welcome Necromancer");

        add("book_of_the_dead.gui.book.page.text.tools.1", "Welcome You Tool!");
        add("book_of_the_dead.gui.book.page.text.tools.2", "Welcome You Tool!");
        add("book_of_the_dead.gui.book.page.text.tools.3", "Welcome You Tool!");

        add("book_of_the_dead.gui.book.page.text.butcher.1", "Welcome Butcher");
        add("book_of_the_dead.gui.book.page.text.butcher.2", "Welcome Butcher");
        add("book_of_the_dead.gui.book.page.text.butcher.3", "Welcome Butcher");

        add("book_of_the_dead.gui.book.page.text.knowledge.1", "Git gud cram alchemy");
        add("book_of_the_dead.gui.book.page.text.knowledge.2", "Git gud cram alchemy");
        add("book_of_the_dead.gui.book.page.text.knowledge.3", "Git gud cram alchemy");
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
