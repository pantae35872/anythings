package com.pantae.anythings.item;

import com.pantae.anythings.Main;
import com.pantae.anythings.abstracts.ModRegistries;
import com.pantae.anythings.enums.ModItemEnums;
import com.pantae.anythings.item.custom.Battery;
import com.pantae.anythings.item.custom.ExplosionItem;
import com.pantae.anythings.item.custom.Bazooka;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems extends ModRegistries<Item, ModItemEnums> {
    private static final DeferredRegister<Item> ITEM = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MOD_ID);
    public ModItems() {
        super(ITEM);
    }
    @Override
    protected void initializeRegistries() {
        MAP.put(ModItemEnums.EXPLOSION_ITEM, register("explosion_item", ExplosionItem::new));
        MAP.put(ModItemEnums.BAZOOKA, register("bazooka", Bazooka::new));
        MAP.put(ModItemEnums.BATTERY, register("battery", Battery::new));
    }
}
