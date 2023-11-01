package dev.sterner.dogma.foundation.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RecipeUtils {
    public static Stream<JsonElement> arrayStream(JsonArray array) {
        return IntStream.range(0, array.size()).mapToObj(array::get);
    }

    public static EntityType<?> deserializeEntityType(JsonObject object) {
        final ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(object, "entity"));
        return ForgeRegistries.ENTITY_TYPES.getValue(id);
    }

    public static NonNullList<Item> deserializeStacks(JsonArray array) {
        if (array.isJsonArray()) {
            return arrayStream(array.getAsJsonArray()).map(entry -> deserializeStack(entry.getAsJsonObject())).collect(DefaultedListCollector.toList());
        } else {
            return NonNullList.of(deserializeStack(array.getAsJsonObject()));
        }
    }

    public static Item deserializeStack(JsonObject object) {
        final ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(object, "item"));
        final Item item = ForgeRegistries.ITEMS.getValue(id);
        if (Items.AIR == item) {
            throw new IllegalStateException("Invalid item: " + item);
        }

        return item;
    }

    public static class DefaultedListCollector<T> implements Collector<T, NonNullList<T>, NonNullList<T>> {

        private static final Set<Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));

        public static <T> DefaultedListCollector<T> toList() {
            return new DefaultedListCollector<>();
        }

        @Override
        public Supplier<NonNullList<T>> supplier() {
            return NonNullList::create;
        }

        @Override
        public BiConsumer<NonNullList<T>, T> accumulator() {
            return NonNullList::add;
        }

        @Override
        public BinaryOperator<NonNullList<T>> combiner() {
            return (left, right) -> {
                left.addAll(right);
                return left;
            };
        }

        @Override
        public Function<NonNullList<T>, NonNullList<T>> finisher() {
            return i -> (NonNullList<T>) i;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return CH_ID;
        }
    }
}
