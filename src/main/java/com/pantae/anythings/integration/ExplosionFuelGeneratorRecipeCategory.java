package com.pantae.anythings.integration;

import com.pantae.anythings.Main;
import com.pantae.anythings.enums.ModBlockEnums;
import com.pantae.anythings.recipe.ExplosionFuelGeneratorRecipe;
import com.pantae.anythings.util.MouseUtil;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.IExtendableCraftingRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.K;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class ExplosionFuelGeneratorRecipeCategory implements IRecipeCategory<ExplosionFuelGeneratorRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(Main.MOD_ID, "explosion_fuel_generator");
    public static final ResourceLocation TEXTURE =
            new ResourceLocation(Main.MOD_ID, "textures/gui/explosion_fuel_generator.png");
    private final IDrawable background;
    private final IDrawable icon;
    private JeiEnergyRender energyRenderer;
    public ExplosionFuelGeneratorRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4, 3, 168, 81);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Main.BLOCKS.BLOCK_ITEM_MAP.get(ModBlockEnums.EXPLOSION_FUEL_GENERATOR).get()));
    }

    @Override
    public RecipeType<ExplosionFuelGeneratorRecipe> getRecipeType() {
        return JEIModPlugin.RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Explosion Fuel Generator");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(ExplosionFuelGeneratorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        Optional<ItemStack> lastStack = recipeSlotsView.getSlotViews(RecipeIngredientRole.INPUT).get(0).getDisplayedItemStack();
        if (this.energyRenderer == null) this.energyRenderer = new JeiEnergyRender(0, 1000000);
        if (lastStack.isPresent()) {
            List<Integer> power = recipe.getPower(lastStack.get());
            if (this.energyRenderer.energy != power.get(0) * power.get(1) * power.get(0))
                    this.energyRenderer = new JeiEnergyRender(power.get(0) * power.get(1) * power.get(0), 1000000);
        } else {
            if (this.energyRenderer.energy != 0) this.energyRenderer = new JeiEnergyRender(0, 1000000);
        }
        this.energyRenderer.draw(guiGraphics, mouseX, mouseY, getWidth(), getHeight(), getBackground().getWidth(), getBackground().getHeight());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ExplosionFuelGeneratorRecipe recipe, IFocusGroup focuses) {
        List<Ingredient> ingredients = recipe.getIngredients();
        IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, 79, 31);
        ingredients.forEach(slot::addIngredients);
    }
}
