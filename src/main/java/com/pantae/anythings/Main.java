package com.pantae.anythings;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import com.pantae.anythings.bakedmodel.CTBModelLoader;
import com.pantae.anythings.block.ModBlocks;
import com.pantae.anythings.block.entity.ModBlockEntities;
import com.pantae.anythings.entity.ModEntities;
import com.pantae.anythings.enums.ModBlockEnums;
import com.pantae.anythings.enums.ModItemEnums;
import com.pantae.anythings.enums.ModTabEnums;
import com.pantae.anythings.item.ModItems;
import com.pantae.anythings.networking.ModNetwork;
import com.pantae.anythings.networking.packet.TestS2CPacket;
import com.pantae.anythings.recipe.ModRecipeSerializers;
import com.pantae.anythings.recipe.ModRecipeTypes;
import com.pantae.anythings.screen.ModMenuTypes;
import com.pantae.anythings.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.Map;

@Mod(Main.MOD_ID)
public class Main
{
    public static final String MOD_ID = "anythings";
    public static final ModItems ITEMS = new ModItems();
    public static final ModCreativeTabs TABS = new ModCreativeTabs();
    public static final ModBlocks BLOCKS = new ModBlocks();
    public static final ModSounds SOUNDS = new ModSounds();
    public static final ModEntities ENTITIES = new ModEntities();
    public static final ModBlockEntities BLOCK_ENTITIES = new ModBlockEntities();
    public static final ModMenuTypes MENU_TYPES = new ModMenuTypes();
    public static final ModRecipeTypes RECIPE_TYPES = new ModRecipeTypes();
    public static final Logger LOGGER = LogUtils.getLogger();


    public Main()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        ITEMS.register(modEventBus);
        TABS.register(modEventBus);
        BLOCKS.register(modEventBus);
        SOUNDS.register(modEventBus);
        ENTITIES.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::commonSetup);
    }
    private void addCreative(@NotNull BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTab() == TABS.getRegistry(ModTabEnums.ANYTHING).get()) {
            for (Map.Entry<ModItemEnums, RegistryObject<Item>> entry: ITEMS.MAP.entrySet()) {
                event.accept(entry.getValue());
            }
            for (Map.Entry<ModBlockEnums, RegistryObject<Item>> entry: BLOCKS.BLOCK_ITEM_MAP.entrySet()) {
                event.accept(entry.getValue());
            }
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModNetwork.register();
    }
}
