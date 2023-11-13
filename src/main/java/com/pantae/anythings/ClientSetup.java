package com.pantae.anythings;

import com.pantae.anythings.bakedmodel.CTBModelLoader;
import com.pantae.anythings.block.client.ExplosionFuelGeneratorRenderer;
import com.pantae.anythings.block.client.model.ExplosionFuelGeneratorModel;
import com.pantae.anythings.entity.client.ExplodingProjectileModel;
import com.pantae.anythings.entity.client.ExplodingProjectileRenderer;
import com.pantae.anythings.enums.ModBlockEntityEnum;
import com.pantae.anythings.enums.ModEntityEnums;
import com.pantae.anythings.enums.ModMenuEnums;
import com.pantae.anythings.screen.ExplosionFuelGeneratorScreen;
import com.pantae.anythings.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void modelInit(ModelEvent.RegisterGeometryLoaders event) {
        CTBModelLoader.register(event);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    }

    @SubscribeEvent
    public static void registerRenderersLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.EXPLODINGPROJECTILE, ExplodingProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.EXPLOSION_FUEL_GENERATOR, ExplosionFuelGeneratorModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(Main.ENTITIES.getRegistry(ModEntityEnums.EXPLODING_PROJECTILE).get(), ExplodingProjectileRenderer::new);
        BlockEntityRenderers.register(Main.BLOCK_ENTITIES.getRegistry(ModBlockEntityEnum.ExplosionFuelGenerator).get(), ExplosionFuelGeneratorRenderer::new);
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.EXPLOSION_FUEL_GENERATOR_MENU.get(),
                    (AbstractContainerMenu menu, Inventory playerInventory, Component title) -> new ExplosionFuelGeneratorScreen<>(menu, playerInventory, title));
        });
    }
}
