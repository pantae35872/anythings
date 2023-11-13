package com.pantae.anythings;

import com.pantae.anythings.abstracts.ModRegistries;
import com.pantae.anythings.enums.ModItemEnums;
import com.pantae.anythings.enums.ModTabEnums;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

public class ModCreativeTabs extends ModRegistries<CreativeModeTab, ModTabEnums> {
    private static final DeferredRegister<CreativeModeTab> TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MOD_ID);

    public ModCreativeTabs() {
        super(TAB);
    }

    @Override
    protected void initializeRegistries() {
        MAP.put(ModTabEnums.ANYTHING, register("anythings", () -> CreativeModeTab.builder()
                .icon(() -> new ItemStack(Main.ITEMS.getRegistry(ModItemEnums.EXPLOSION_ITEM).get()))
                .title(Component.literal("AnyThings")).build()));
    }
}
