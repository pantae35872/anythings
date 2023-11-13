package com.pantae.anythings.datagen;

import com.pantae.anythings.Main;
import com.pantae.anythings.enums.ModBlockEnums;
import com.pantae.anythings.enums.ModItemEnums;
import com.pantae.anythings.enums.ModSoundEnums;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output , String locale) {
        super(output, Main.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        for (Map.Entry<ModItemEnums, RegistryObject<Item>> entry : Main.ITEMS.MAP.entrySet()) {
            addItem(entry.getValue());
        }
        for (Map.Entry<ModBlockEnums, RegistryObject<Item>> entry : Main.BLOCKS.BLOCK_ITEM_MAP.entrySet()) {
            addItem(entry.getValue());
        }
        for (Map.Entry<ModSoundEnums, RegistryObject<SoundEvent>> entry : Main.SOUNDS.MAP.entrySet()) {
            addSound(entry.getValue());
        }
    }
    private void addItem(RegistryObject<Item> item) {
        this.add(item.get(), calculateName(item.getId().getPath()));
    }

    private String calculateName(String id) {
        char[] item_char = id.toCharArray();
        item_char[0] = Character.toUpperCase(item_char[0]);
        for (int i = 0; i < item_char.length; i++) {
            if (item_char[i] == '_') {
                item_char[i] = ' ';
                char next_char = item_char[i+1];
                item_char[i+1] = Character.toUpperCase(next_char);
            }
        }

        return String.valueOf(item_char);
    }

    private void addSound(RegistryObject<SoundEvent> key) {
        this.add("sounds." + Main.MOD_ID + "." + key.getId().getPath(), calculateName(key.getId().getPath()));
    }
}
