package com.pantae.anythings.datagen;

import com.pantae.anythings.Main;
import com.pantae.anythings.enums.ModBlockEnums;
import com.pantae.anythings.enums.ModItemEnums;
import com.pantae.anythings.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (Map.Entry<ModItemEnums, RegistryObject<Item>> entry : Main.ITEMS.MAP.entrySet()) {
            if (entry.getKey().equals(ModItemEnums.BAZOOKA)) {
                weaponItem(entry.getValue());
            } else {
                simpleItem(entry.getValue());
            }
        }
    }

    private void simpleItem(RegistryObject<Item> item) {
        withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Main.MOD_ID, "item/" + item.getId().getPath()));
    }

    private void weaponItem(RegistryObject<Item> item) {
        withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Main.MOD_ID, "item/" + item.getId().getPath()));
    }
}
