package com.pantae.anythings.recipe;

import com.google.common.eventbus.EventBus;
import com.pantae.anythings.Main;
import com.pantae.anythings.abstracts.ModRegistries;
import com.pantae.anythings.enums.ModRecipeEnums;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Main.MOD_ID);
    public static final RegistryObject<RecipeSerializer<ExplosionFuelGeneratorRecipe>> EXPLOSION_FUEL_RECIPE = RECIPE_SERIALIZER.register("explosion_fuel_generator", () ->
            new ExplosionFuelGeneratorSerializer<>(ExplosionFuelGeneratorRecipe::new));

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZER.register(eventBus);
    }
}
