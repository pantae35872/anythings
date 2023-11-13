package com.pantae.anythings.integration;

import com.pantae.anythings.Main;
import com.pantae.anythings.enums.ModBlockEnums;
import com.pantae.anythings.enums.ModMenuEnums;
import com.pantae.anythings.recipe.ExplosionFuelGeneratorRecipe;
import com.pantae.anythings.screen.ExplosionFuelGeneratorMenu;
import com.pantae.anythings.screen.ExplosionFuelGeneratorScreen;
import com.pantae.anythings.screen.ModMenuTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIModPlugin implements IModPlugin {
    public static RecipeType<ExplosionFuelGeneratorRecipe> RECIPE_TYPE = new RecipeType<>(ExplosionFuelGeneratorRecipeCategory.UID,
            ExplosionFuelGeneratorRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Main.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ExplosionFuelGeneratorRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()
        ));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ExplosionFuelGeneratorScreen.class, 106, 33, 20, 18, RECIPE_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Main.BLOCKS.BLOCK_ITEM_MAP.get(ModBlockEnums.EXPLOSION_FUEL_GENERATOR).get()), RECIPE_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ExplosionFuelGeneratorMenu.class,
                ModMenuTypes.EXPLOSION_FUEL_GENERATOR_MENU.get(), RECIPE_TYPE, 36, 1, 0, 36);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Minecraft.getInstance().level.getRecipeManager();

        List<ExplosionFuelGeneratorRecipe> recipesInfusing = new ArrayList<>();

        for (ExplosionFuelGeneratorRecipe recipe : rm.getAllRecipesFor(ExplosionFuelGeneratorRecipe.Type.INSTANCE).stream()
                .map(RecipeHolder::value).toList()) {
            for (int i = 0; i < recipe.recipeItems.size(); i++) {
                Ingredient ingredient = recipe.recipeItems.get(i);
                List<Integer> output = recipe.output.get(i);
                ExplosionFuelGeneratorRecipe newRecipe = new ExplosionFuelGeneratorRecipe(
                        new ArrayList<>() {{ add(ingredient);}}, new ArrayList<>() {{ add(output);}});
                recipesInfusing.add(newRecipe);
            }
        }

        registration.addRecipes(RECIPE_TYPE, recipesInfusing);
    }
}

