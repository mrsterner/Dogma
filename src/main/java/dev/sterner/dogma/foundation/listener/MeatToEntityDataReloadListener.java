package dev.sterner.dogma.foundation.listener;

import com.google.gson.*;
import dev.sterner.dogma.foundation.util.RecipeUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class MeatToEntityDataReloadListener extends SimpleJsonResourceReloadListener {

    public static final Map<ResourceLocation, MeatData> MEAT_DATA = new HashMap<>();

    private static final Gson GSON = (new GsonBuilder()).create();

    public MeatToEntityDataReloadListener() {
        super(GSON, "meat_to_entity_data");
    }

    public static void register(AddReloadListenerEvent event) {
        event.addListener(new MeatToEntityDataReloadListener());
    }

    @Nullable
    public static EntityType<?> getEntity(Item item) {
        EntityType<?> out = null;

        for (Map.Entry<ResourceLocation, MeatData> meatDataEntry : MEAT_DATA.entrySet()) {
            NonNullList<Item> meatList = meatDataEntry.getValue().items;
            if (meatList.contains(item)) {
                ResourceLocation resourceLocation = meatDataEntry.getKey();
                out = ForgeRegistries.ENTITY_TYPES.getValue(resourceLocation);
            }
        }
        return out;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        MEAT_DATA.clear();
        for (JsonElement entry : pObject.values()) {
            JsonObject object = entry.getAsJsonObject();
            String name = object.getAsJsonPrimitive("entity_type").getAsString();
            ResourceLocation resourceLocation = new ResourceLocation(name);
            if (!ForgeRegistries.ENTITY_TYPES.containsKey(resourceLocation)) {
                continue;
            }
            JsonArray array = object.getAsJsonArray("meats");
            MEAT_DATA.put(resourceLocation, new MeatData(RecipeUtils.deserializeStacks(array)));
        }
    }

    public static class MeatData {

        public NonNullList<Item> items;

        public MeatData(NonNullList<Item> items) {
            this.items = items;
        }
    }
}