package dev.sterner.dogma.foundation.recipe.necro;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.sterner.dogma.api.StackWithChance;
import dev.sterner.dogma.foundation.registry.DogmaRecipeRegistry;
import dev.sterner.dogma.foundation.util.RecipeUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.recipe.ILodestoneRecipe;

import java.util.List;

public class ButcheringRecipe extends ILodestoneRecipe {
    public static final String NAME = "butchering";
    private final ResourceLocation id;
    private final EntityType<?> entityType;
    private final NonNullList<StackWithChance> outputs;
    private final StackWithChance headDrop;

    public ButcheringRecipe(ResourceLocation id, EntityType<?> entityType, NonNullList<StackWithChance> outputs, StackWithChance headDrop){
        this.id = id;
        this.entityType = entityType;
        this.outputs = outputs;
        this.headDrop = headDrop;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DogmaRecipeRegistry.BUTCHERING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return DogmaRecipeRegistry.BUTCHERING_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ButcheringRecipe> {

        @Override
        public ButcheringRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(GsonHelper.getAsString(json, "entityType")));

            JsonArray array = GsonHelper.getAsJsonArray(json, "results");
            NonNullList<StackWithChance> outputs = RecipeUtils.deserializeStackWithCount(array);
            if (outputs.isEmpty()) {
                throw new JsonParseException("No output for Butchering");
            } else if (outputs.size() > 8) {
                throw new JsonParseException("Too many outputs for Butchering recipe");
            }

            StackWithChance headDrop = new StackWithChance(ItemStack.EMPTY, 1.0f);
            if (GsonHelper.isArrayNode(json, "head")) {
                JsonArray headArray = GsonHelper.getAsJsonArray(json, "head");
                headDrop = RecipeUtils.deserializeStackWithCount(headArray).get(0);
            }

            return new ButcheringRecipe(pRecipeId, entityType, outputs, headDrop);
        }

        @Override
        public @Nullable ButcheringRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            //EntityType
            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(pBuffer.readUtf()));

            //Outputs
            NonNullList<StackWithChance> outputs = NonNullList.withSize(pBuffer.readInt(), new StackWithChance(ItemStack.EMPTY, 1.0F));
            outputs.replaceAll(ignored -> new StackWithChance(pBuffer.readItem(), pBuffer.readFloat()));

            //Output Head
            StackWithChance headDrop = new StackWithChance(pBuffer.readItem(), pBuffer.readFloat());

            return new ButcheringRecipe(pRecipeId, entityType, outputs, headDrop);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ButcheringRecipe pRecipe) {
            //Entity Type
            pBuffer.writeUtf(ForgeRegistries.ENTITY_TYPES.getDelegate(pRecipe.entityType).toString());

            //Outputs
            pBuffer.writeInt(pRecipe.outputs.size());
            for (StackWithChance pair : pRecipe.outputs) {
                pBuffer.writeItem(pair.itemStack());
                pBuffer.writeFloat(pair.chance());
            }

            //Output Head
            pBuffer.writeItem(pRecipe.headDrop.itemStack());
            pBuffer.writeFloat(pRecipe.headDrop.chance());
        }
    }
}
