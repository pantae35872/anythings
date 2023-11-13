package com.pantae.anythings.datagen;

import com.pantae.anythings.Main;
import com.pantae.anythings.block.ModBlocks;
import com.pantae.anythings.enums.ModBlockEnums;
import com.pantae.anythings.enums.ModItemEnums;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ForgeItemTagsProvider;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(RecipeOutput pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Main.ITEMS.getRegistry(ModItemEnums.EXPLOSION_ITEM).get(), 8)
                .pattern("AEA")
                .pattern("ECE")
                .pattern("AEA")
                .define('A', Items.FLINT_AND_STEEL)
                .define('E', Items.TNT)
                .define('C', Items.SAND)
                .unlockedBy(getHasName(Items.FLINT_AND_STEEL), has(Items.FLINT_AND_STEEL))
                .unlockedBy(getHasName(Items.TNT), has(Items.TNT))
                .unlockedBy(getHasName(Items.SAND), has(Items.SAND))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Main.ITEMS.getRegistry(ModItemEnums.BAZOOKA).get())
                .pattern("A A")
                .pattern("AEA")
                .pattern("AAA")
                .define('A', Items.IRON_INGOT)
                .define('E', Items.GUNPOWDER)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(Items.GUNPOWDER), has(Items.GUNPOWDER))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Main.BLOCKS.BLOCK_ITEM_MAP.get(ModBlockEnums.EXPLOSION_FUEL_GENERATOR).get())
                .pattern("AEA")
                .pattern("ERE")
                .pattern("AEA")
                .define('A', Items.OBSIDIAN)
                .define('E', Tags.Items.GLASS_PANES)
                .define('R', Items.PISTON)
                .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
                .unlockedBy(getHasName(Items.PISTON), has(Items.PISTON))
                .save(pWriter);
    }
}
