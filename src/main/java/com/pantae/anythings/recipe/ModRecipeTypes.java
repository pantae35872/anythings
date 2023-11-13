package com.pantae.anythings.recipe;

import com.pantae.anythings.Main;
import com.pantae.anythings.abstracts.ModRegistries;
import com.pantae.anythings.enums.ModRecipeEnums;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeTypes extends ModRegistries<RecipeType<?>, ModRecipeEnums> {
    private static final DeferredRegister<RecipeType<?>> RECIPE = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Main.MOD_ID);
    public ModRecipeTypes() {
        super(RECIPE);
    }

    @Override
    protected void initializeRegistries() {
        this.MAP.put(ModRecipeEnums.EXPLODING_FUEL_GENERATOR_RECIPE, this.register("explosion_fuel_generator", () -> ExplosionFuelGeneratorRecipe.Type.INSTANCE));
    }
}
