package com.pantae.anythings.bakedmodel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pantae.anythings.Main;
import com.pantae.anythings.block.CTBBlock;
import com.pantae.anythings.enums.ModConnectedTextureEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.pantae.anythings.enums.ModConnectedTextureEnum.NONE;
import static com.pantae.anythings.enums.ModConnectedTextureEnum.SIDE;
import static com.pantae.anythings.helper.BakedModelHelper.quad;
import static com.pantae.anythings.helper.BakedModelHelper.v;

@OnlyIn(Dist.CLIENT)
public class CTBBakedModel implements IDynamicBakedModel {

    private final IGeometryBakingContext context;
    private final String block_name;
    private boolean use_default;
    private TextureAtlasSprite spriteNone;
    private TextureAtlasSprite spriteNormal;
    private TextureAtlasSprite spriteEnd;
    private TextureAtlasSprite spriteCorner;
    private TextureAtlasSprite spriteThree;
    private TextureAtlasSprite spriteSide;
    private final Map<List<ModConnectedTextureEnum>, List<BakedQuad>> cache = new ConcurrentHashMap<>();
    private List<BakedQuad> item_model_quads = new ArrayList<>();
    public CTBBakedModel(IGeometryBakingContext context, String block_name, boolean use_default) {
        this.context = context;
        this.block_name = block_name;
        this.use_default = use_default;
    }

    private void initTextures() {
            spriteNormal = getTexture(MessageFormat.format("block/{0}/normal", block_name));
            spriteNone= getTexture(MessageFormat.format("block/{0}/none", block_name));
            spriteEnd = getTexture(MessageFormat.format("block/{0}/end", block_name));
            spriteCorner = getTexture(MessageFormat.format("block/{0}/corner", block_name));
            spriteThree = getTexture(MessageFormat.format("block/{0}/three", block_name));
            spriteSide = getTexture(MessageFormat.format("block/{0}/side", block_name));
    }

    private List<BakedQuad> getCachedQuads(List<ModConnectedTextureEnum> sideList) {
        return cache.computeIfAbsent(sideList, this::compute_quads);
    }
    private List<BakedQuad> compute_quads(List<ModConnectedTextureEnum> side) {
        List<BakedQuad> quads = new ArrayList<>();
        ModConnectedTextureEnum up = side.get(0);
        ModConnectedTextureEnum down = side.get(1);
        ModConnectedTextureEnum north = side.get(2);
        ModConnectedTextureEnum south = side.get(3);
        ModConnectedTextureEnum east = side.get(4);
        ModConnectedTextureEnum west = side.get(5);
        quads.add(quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNormal));
        quads.add(quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNormal));
        quads.add(quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNormal));
        quads.add(quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNormal));
        quads.add(quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNormal));
        quads.add(quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNormal));
        if (up == NONE && down == NONE && north == SIDE && south == SIDE && east == SIDE && west == SIDE) {
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteSide, 0));
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteSide, 0));
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteSide, 1));
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteSide, 1));
        }
        if (up == NONE && down == SIDE && north == SIDE && south == SIDE && east == SIDE && west == SIDE) {
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNormal));
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteThree, 3));
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteThree, 3));
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteThree, 2));
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteThree, 0));
        }
        if (up == SIDE && down == NONE && north == SIDE && south == SIDE && east == SIDE && west == SIDE) {
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNormal));
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteThree, 1));
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteThree, 1));
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteThree, 0));
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteThree, 2));
        }
        if (up == SIDE && down == SIDE && north == NONE && south == SIDE && east == SIDE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteThree));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteThree, 2));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteThree, 2));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteThree));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone, 0));
            //east
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNormal, 2));
        }
        if (up == SIDE && down == SIDE && north == SIDE && south == NONE && east == SIDE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteThree, 2));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteThree));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteThree));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteThree, 2));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNormal, 0));
            //east
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone, 2));
        }
        if (up == SIDE && down == SIDE && north == NONE && south == NONE && east == SIDE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteSide, 1));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteSide, 1));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteSide, 1));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteSide, 1));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //east
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == SIDE && down == SIDE && north == SIDE && south == SIDE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteThree, 3));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteThree,3));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNormal));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteThree, 3));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteThree, 3));
        }
        if (up == SIDE && down == SIDE && north == SIDE && south == SIDE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteThree, 1));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteThree,1));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNormal));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteThree, 1));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteThree, 1));
        }
        if (up == SIDE && down == SIDE && north == SIDE && south == SIDE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteSide));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteSide));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteSide));
            //east
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteSide));
        }
        //Two Way
        if (up == SIDE && down == SIDE && north == SIDE && south == NONE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteCorner, 1));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteCorner));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteThree));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteThree, 1));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == SIDE && down == SIDE && north == SIDE && south == NONE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteCorner, 2));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteCorner, 3));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteThree, 2));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteThree, 3));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == SIDE && down == SIDE && north == NONE && south == SIDE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteCorner, 3));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteCorner,2));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteThree));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteThree,3));
        }
        if (up == SIDE && down == SIDE && north == NONE && south == SIDE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteCorner));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteCorner, 1));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteThree, 2));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteThree,1));
        }
        if (up == NONE && down == SIDE && north == NONE && south == SIDE && east == SIDE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteThree, 1));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteCorner, 2));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteCorner, 3));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteThree));
        }

        if (up == SIDE && down == NONE && north == NONE && south == SIDE && east == SIDE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteThree));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteCorner, 1));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteCorner));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteThree, 2));
        }

        if (up == SIDE && down == NONE && north == SIDE && south == NONE && east == SIDE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteThree, 2));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteCorner));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteCorner, 1));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteThree));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone, 2));
        }
        if (up == NONE && down == SIDE && north == SIDE && south == NONE && east == SIDE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteThree));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteCorner, 3));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteCorner, 2));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteThree, 2));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone, 2));
        }

        if (up == NONE && down == SIDE && north == SIDE && south == SIDE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteThree, 1));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteThree, 3));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone, 2));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteCorner, 1));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteCorner));
        }

        if (up == NONE && down == SIDE && north == SIDE && south == SIDE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteThree, 3));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone, 3));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteThree, 3));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteCorner, 2));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteCorner, 3));
        }
        if (up == SIDE && down == NONE && north == SIDE && south == SIDE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteThree, 3));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone, 3));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone, 3));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteThree, 1));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteCorner, 3));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteCorner, 2));
        }

        if (up == SIDE && down == NONE && north == SIDE && south == SIDE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteThree, 1));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone, 3));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteThree, 1));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone, 1));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteCorner));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteCorner, 1));
        }

        //three-way
        if (up == SIDE && down == NONE && north == SIDE && south == NONE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteCorner, 2));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteCorner, 1));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteCorner, 3));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == SIDE && down == NONE && north == SIDE && south == NONE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteCorner, 1));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteCorner));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone, 1));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteCorner));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == NONE && down == SIDE && north == SIDE && south == NONE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteCorner, 3));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteCorner, 2));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteCorner, 2));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == NONE && down == SIDE && north == SIDE && south == NONE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteCorner));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteCorner, 3));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteCorner, 1));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == SIDE && down == NONE && north == NONE && south == SIDE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteCorner, 3));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteCorner));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteCorner, 2));
        }
        if (up == SIDE && down == NONE && north == NONE && south == SIDE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteCorner));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteCorner, 1));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteCorner, 1));
        }
        if (up == NONE && down == SIDE && north == NONE && south == SIDE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteCorner, 1));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteCorner, 2));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteCorner));
        }
        if (up == NONE && down == SIDE && north == NONE && south == SIDE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteCorner, 2));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteCorner, 3));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteCorner, 3));
        }
        if (up == NONE && down == NONE && north == SIDE && south == SIDE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteSide));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteEnd, 1));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteEnd, 1));
        }
        if (up == NONE && down == NONE && north == SIDE && south == SIDE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteSide));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteEnd, 3));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteEnd, 3));
        }
        if (up == NONE && down == NONE && north == NONE && south == SIDE && east == SIDE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteEnd));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteEnd, 2));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteSide, 1));
        }
        if (up == NONE && down == NONE && north == SIDE && south == NONE && east == SIDE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteEnd, 2));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteEnd));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteSide, 1));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == SIDE && down == SIDE && north == NONE && south == NONE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteEnd, 1));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteEnd, 1));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteSide, 1));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == SIDE && down == SIDE && north == NONE && south == SIDE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteEnd, 2));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteEnd));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteSide));
        }

        if (up == SIDE && down == SIDE && north == NONE && south == NONE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteEnd, 3));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteEnd, 3));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteSide, 1));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }

        if (up == SIDE && down == SIDE && north == SIDE && south == NONE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteEnd));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteEnd, 2));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteSide));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }

        if (up == SIDE && down == NONE && north == SIDE && south == SIDE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteSide));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteEnd, 2));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteEnd));
        }

        if (up == SIDE && down == NONE && north == NONE && south == NONE && east == SIDE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteSide, 1));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteEnd, 3));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteEnd, 3));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }

        if (up == NONE && down == SIDE && north == SIDE && south == SIDE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteSide));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteEnd));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteEnd, 2));
        }

        if (up == NONE && down == SIDE && north == NONE && south == NONE && east == SIDE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteSide, 1));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteEnd, 1));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteEnd, 1));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        //four-way
        if (up == NONE && down == NONE && north == SIDE && south == NONE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteEnd, 2));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteEnd, 3));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == NONE && down == NONE && north == NONE && south == SIDE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteEnd, 2));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteEnd, 1));
        }
        if (up == NONE && down == NONE && north == NONE && south == SIDE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteEnd));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteEnd, 3));
        }

        if (up == NONE && down == NONE && north == SIDE && south == NONE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteEnd));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteEnd, 1));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }

        if (up == NONE && down == SIDE && north == NONE && south == SIDE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteEnd));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteEnd, 2));
        }

        if (up == NONE && down == SIDE && north == SIDE && south == NONE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteEnd, 2));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteEnd));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }

        if (up == NONE && down == SIDE && north == NONE && south == NONE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteEnd, 1));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteEnd, 1));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == NONE && down == SIDE && north == NONE && south == NONE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteEnd, 3));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteEnd, 1));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == SIDE && down == SIDE && north == NONE && south == NONE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == NONE && down == NONE && north == NONE && south == NONE && east == SIDE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }

        if (up == NONE && down == NONE && north == SIDE && south == SIDE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }

        if (up == SIDE && down == NONE && north == SIDE && south == NONE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteEnd));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteEnd, 2));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == SIDE && down == NONE && north == NONE && south == NONE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteEnd, 3));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteEnd, 3));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == SIDE && down == NONE && north == NONE && south == NONE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteEnd, 1));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteEnd, 3));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == SIDE && down == NONE && north == NONE && south == SIDE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteEnd, 2));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteEnd));
        }
        if (up == NONE && down == NONE && north == NONE && south == NONE && east == SIDE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == NONE && down == NONE && north == NONE && south == NONE && east == NONE && west == SIDE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == NONE && down == NONE && north == NONE && south == SIDE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == NONE && down == NONE && north == SIDE && south == NONE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == NONE && down == NONE && north == NONE && south == NONE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        if (up == SIDE && down == NONE && north == NONE && south == NONE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }

        if (up == NONE && down == SIDE && north == NONE && south == NONE && east == NONE && west == NONE) {
            //up
            quads.set(0, quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteNone));
            //down
            quads.set(1,quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteNone));
            //east
            quads.set(2, quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteNone));
            //west
            quads.set(3, quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteNone));
            //north
            quads.set(4,quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteNone));
            //south
            quads.set(5,quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteNone));
        }
        return quads;
    }

    private TextureAtlasSprite getTexture(String path) {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(Main.MOD_ID, path));
    }
    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                             @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
        initTextures();
        if (this.cache.isEmpty() && !this.use_default) {
            for (ModConnectedTextureEnum up : ModConnectedTextureEnum.values()) {
                for (ModConnectedTextureEnum down : ModConnectedTextureEnum.values()) {
                    for (ModConnectedTextureEnum north : ModConnectedTextureEnum.values()) {
                        for (ModConnectedTextureEnum south : ModConnectedTextureEnum.values()) {
                            for (ModConnectedTextureEnum west : ModConnectedTextureEnum.values()) {
                                for (ModConnectedTextureEnum east : ModConnectedTextureEnum.values()) {
                                    List<ModConnectedTextureEnum> sideList = new ArrayList<>() {{
                                        add(up);
                                        add(down);
                                        add(north);
                                        add(south);
                                        add(east);
                                        add(west);
                                    }};
                                    this.cache.put(sideList, compute_quads(sideList));
                                }
                            }
                        }
                    }
                }
            }
        } else if (this.cache.isEmpty()) {
            if (this.item_model_quads.isEmpty()) {
                List<ModConnectedTextureEnum> sideList = new ArrayList<>() {{
                    add(SIDE);
                    add(SIDE);
                    add(SIDE);
                    add(SIDE);
                    add(SIDE);
                    add(SIDE);
                }};

                this.item_model_quads = compute_quads(sideList);
            }
            return this.item_model_quads;
        }
        List<BakedQuad> quads = new ArrayList<>();
        if (side == null && (renderType == null || renderType.equals(RenderType.solid()))) {
            ModConnectedTextureEnum north, south, west, east, up, down;
            if (state != null) {
                north = state.getValue(CTBBlock.NORTH);
                south = state.getValue(CTBBlock.SOUTH);
                east = state.getValue(CTBBlock.EAST);
                west = state.getValue(CTBBlock.WEST);
                up = state.getValue(CTBBlock.UP);
                down = state.getValue(CTBBlock.DOWN);
            } else {
                north = south = west = east = up = down = NONE;
            }

            List<ModConnectedTextureEnum> sideList = new ArrayList<>() {{
                add(up);
                add(down);
                add(north);
                add(south);
                add(east);
                add(west);
            }};
            quads = getCachedQuads(sideList);
        }
        return quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return ChunkRenderTypeSet.all();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return spriteNormal == null
                ? Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply((new ResourceLocation("minecraft", "missingno")))
                : spriteNormal;
    }

    @Override
    public @NotNull BakedModel applyTransform(@NotNull ItemDisplayContext transformType, @NotNull PoseStack poseStack, boolean applyLeftHandTransform) {
        ItemTransform transform = context.getTransforms().getTransform(transformType);
        if (transformType != ItemDisplayContext.GUI) {
            poseStack.scale(transform.scale.x, transform.scale.y, transform.scale.z);
            poseStack.translate(transform.translation.x, transform.translation.y, transform.translation.z);
            poseStack.mulPose(new Quaternionf().rotateX(transform.rotation.x).rotationY(transform.rotation.y).rotateZ(transform.rotation.z));
        } else {
            poseStack.scale(0.65f,0.65f,0.65f);
            poseStack.rotateAround(new Quaternionf().rotateX(-1.1f).rotateY(0).rotateZ(-0.8f), 0,0,0);
        }
        this.use_default = true;
        return this;
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}
