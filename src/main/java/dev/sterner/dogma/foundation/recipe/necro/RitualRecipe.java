package dev.sterner.dogma.foundation.recipe.necro;

import dev.sterner.dogma.api.CommandType;
import dev.sterner.dogma.foundation.necro.BasicNecrotableRitual;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import team.lodestar.lodestone.systems.recipe.ILodestoneRecipe;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class RitualRecipe extends ILodestoneRecipe {

    public ResourceLocation id;
    public BasicNecrotableRitual ritual;
    public ResourceLocation texture;
    public boolean requireBotD;
    public boolean requireEmeraldTablet;
    public boolean isSpecial;
    public int duration;
    public @Nullable NonNullList<Ingredient> inputs;
    public @Nullable List<ItemStack> outputs;
    public @Nullable List<EntityType<?>> sacrifices;
    public @Nullable List<EntityType<?>> summons;
    public @Nullable List<MobEffectInstance> statusEffectInstance;
    public Set<CommandType> command;

    public RitualRecipe(ResourceLocation id,
                        BasicNecrotableRitual ritual,
                        ResourceLocation texture,
                        boolean requireBotD,
                        boolean requireEmeraldTablet,
                        boolean isSpecial,
                        int duration,
                        @Nullable NonNullList<Ingredient> inputs,
                        @Nullable List<ItemStack> outputs,
                        @Nullable List<EntityType<?>> sacrifices,
                        @Nullable List<EntityType<?>> summons,
                        @Nullable List<MobEffectInstance> statusEffectInstance,
                        Set<CommandType> command){
        this.id = id;
        this.ritual = ritual;
        this.texture = texture;
        this.requireBotD = requireBotD;
        this.requireEmeraldTablet = requireEmeraldTablet;
        this.isSpecial = isSpecial;
        this.duration = duration;
        this.inputs = inputs;
        this.outputs = outputs;
        this.sacrifices = sacrifices;
        this.summons = summons;
        this.statusEffectInstance = statusEffectInstance;
        this.command = command;
    }
    
    @Override
    public ResourceLocation getId() {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }
}
