package com.pantae.anythings.recipe;

import com.pantae.anythings.Main;
import com.pantae.anythings.enums.ModRecipeEnums;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExplosionFuelGeneratorRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    public final List<Ingredient> recipeItems;
    public final List<List<Integer>> output;

    public ExplosionFuelGeneratorRecipe(List<Ingredient> recipeItems, List<List<Integer>> output) {
        this.id = new ResourceLocation(Main.MOD_ID, "explosion_fuel_generator");
        this.recipeItems = recipeItems;
        this.output = output;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide()) {
            return false;
        }

        return recipeItems.stream().anyMatch(ingredient -> ingredient.test(pContainer.getItem(0)));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonNullList = NonNullList.withSize(recipeItems.size(), recipeItems.get(0));
        for (int i = 0; i < recipeItems.size(); i++) {
            nonNullList.set(i, recipeItems.get(i));
        }
        return nonNullList;
    }

    public List<Integer> getPower(ItemStack stack) {
        for (int i = 0; i < recipeItems.size(); i++) {
            if (recipeItems.get(i).test(stack)) {
                return output.get(i);
            }
        }
        return null;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return null;
    }

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.EXPLOSION_FUEL_RECIPE.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ExplosionFuelGeneratorRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "explosion_fuel_generator";
    }
}
