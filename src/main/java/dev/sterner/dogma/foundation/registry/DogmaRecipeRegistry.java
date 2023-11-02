package dev.sterner.dogma.foundation.registry;

import dev.sterner.dogma.Dogma;
import dev.sterner.dogma.foundation.recipe.necro.ButcheringRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface DogmaRecipeRegistry {
    DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Dogma.MODID);
    DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Dogma.MODID);
    //start common

    //end common
    //------------------------------------------------
    //start abyss

    //end abyss
    //------------------------------------------------
    //start necro
    RegistryObject<RecipeType<ButcheringRecipe>> BUTCHERING_TYPE = RECIPE_TYPES.register(ButcheringRecipe.NAME, () -> registerRecipeType(ButcheringRecipe.NAME));
    RegistryObject<RecipeSerializer<ButcheringRecipe>> BUTCHERING_SERIALIZER = RECIPE_SERIALIZERS.register(ButcheringRecipe.NAME, ButcheringRecipe.Serializer::new);

    //end necro
    //------------------------------------------------
    static <T extends Recipe<?>> RecipeType<T> registerRecipeType(final String identifier) {
        return new RecipeType<>() {
            public String toString() {
                return Dogma.MODID + ":" + identifier;
            }
        };
    }
}
