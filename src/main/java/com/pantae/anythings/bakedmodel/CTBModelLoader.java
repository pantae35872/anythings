package com.pantae.anythings.bakedmodel;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.pantae.anythings.Main;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.Function;

public class CTBModelLoader implements IGeometryLoader<CTBModelLoader.CTBModelGeometry> {
    public static final ResourceLocation GENERATOR_LOADER = new ResourceLocation(Main.MOD_ID, "connectedtextureloader");


    public static void register(ModelEvent.RegisterGeometryLoaders event) {
        event.register("connectedtextureloader", new CTBModelLoader());
    }

    @Override
    public CTBModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        String block_name = "";
        boolean use_default = false;
        if (jsonObject.has("block_name")) block_name = jsonObject.get("block_name").getAsString();
        if (jsonObject.has("use_default")) use_default = jsonObject.get("use_default").getAsBoolean();
        return new CTBModelGeometry(block_name, use_default);
    }

    public static class CTBModelGeometry implements IUnbakedGeometry<CTBModelGeometry> {
        private final String block_name;
        private final boolean use_default;

        public CTBModelGeometry(String block_name, boolean use_default) {
            this.block_name = block_name;
            this.use_default = use_default;
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
            return new CTBBakedModel(context, block_name, use_default);
        }
    }
}
