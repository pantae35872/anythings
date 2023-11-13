package com.pantae.anythings.screen;

import com.pantae.anythings.Main;
import com.pantae.anythings.abstracts.ModRegistries;
import com.pantae.anythings.enums.ModMenuEnums;
import com.pantae.anythings.enums.ModPacketS2CEnum;
import com.pantae.anythings.networking.PendingPacket;
import com.pantae.anythings.networking.packet.ExplosionFuelGeneratorBlockPosS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.Objects;

public class ModMenuTypes extends ModRegistries<MenuType<?>, ModMenuEnums> {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Main.MOD_ID);
    public ModMenuTypes() {
        super(MENUS);
    }

    public static final RegistryObject<MenuType<ExplosionFuelGeneratorMenu>> EXPLOSION_FUEL_GENERATOR_MENU = MENUS.register("explosion_fuel_generator", () ->
            new MenuType<>((inv, player) -> {
                BlockPos pos = ((ExplosionFuelGeneratorBlockPosS2CPacket)
                        Objects.requireNonNull(PendingPacket.getS2C(ModPacketS2CEnum.ExplosionFuelGeneratorBlockPosS2C))).blockPos;
                return new ExplosionFuelGeneratorMenu(inv, player, pos);
            }, FeatureFlags.DEFAULT_FLAGS));

    @Override
    protected void initializeRegistries() {
    }
}
