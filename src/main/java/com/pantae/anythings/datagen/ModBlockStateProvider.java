package com.pantae.anythings.datagen;

import com.google.gson.JsonObject;
import com.mojang.datafixers.TypeRewriteRule;
import com.pantae.anythings.Main;
import com.pantae.anythings.bakedmodel.CTBModelLoader;
import com.pantae.anythings.block.CTBBlock;
import com.pantae.anythings.enums.ModBlockEnums;
import com.pantae.anythings.enums.ModConnectedTextureEnum;
import com.pantae.anythings.enums.ModItemEnums;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Main.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (Map.Entry<ModBlockEnums, RegistryObject<Block>> entry : Main.BLOCKS.MAP.entrySet()) {
            RegistryObject<Block> block = entry.getValue();
            if (block.get() instanceof CTBBlock) {
                registerConnectedTexture(block);
            } else if (block.get() instanceof BaseEntityBlock) {
                simpleBlock(block.get(), new ModelFile.ExistingModelFile(new ResourceLocation(Main.MOD_ID, "block/"+block.getId().getPath()), this.models().existingFileHelper));
                simpleBlockItem(block.get(), new ModelFile.ExistingModelFile(new ResourceLocation(Main.MOD_ID, "item/" +
                        block.getId().getPath() + "_item_model"), this.models().existingFileHelper));
            } else {
                simpleBlockWithItem(block);
            }
        }
    }

    private void simpleBlockWithItem(RegistryObject<Block> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }

    private void registerConnectedTexture(RegistryObject<Block> block) {
        BlockModelBuilder model = models().getBuilder(block.getId().getPath())
                .parent(models().getExistingFile(mcLoc("cube")))
                .customLoader((builder, helper) -> new CableLoaderBuilder(CTBModelLoader.GENERATOR_LOADER, builder, helper, block.getId().getPath(), false))
                .end();
        simpleBlock(block.get(), model);
        itemModels().withExistingParent(block.getId().getPath(), new ResourceLocation(Main.MOD_ID, "block/"+block.getId().getPath()));
    }

    public static class CableLoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {
        private final String block_name;
        private final boolean use_default;
        public CableLoaderBuilder(ResourceLocation loader, BlockModelBuilder parent, ExistingFileHelper existingFileHelper, String block_name, boolean use_default) {
            super(loader, parent, existingFileHelper);
            this.block_name = block_name;
            this.use_default = use_default;
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            JsonObject obj = super.toJson(json);
            obj.addProperty("block_name", block_name);
            obj.addProperty("use_default", use_default);
            return super.toJson(json);
        }
    }
}
