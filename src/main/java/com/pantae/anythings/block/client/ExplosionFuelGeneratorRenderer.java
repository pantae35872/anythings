package com.pantae.anythings.block.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pantae.anythings.Main;
import com.pantae.anythings.ModModelLayers;
import com.pantae.anythings.block.client.model.ExplosionFuelGeneratorModel;
import com.pantae.anythings.block.custom.ExplosionFuelGeneratorBlock;
import com.pantae.anythings.block.entity.ExplosionFuelGeneratorBlockEntity;
import com.pantae.anythings.entity.client.ExplodingProjectileModel;
import com.pantae.anythings.entity.custom.ExplodingProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWNativeX11;
import org.lwjgl.opengl.GL11;

public class ExplosionFuelGeneratorRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    private BlockEntityRendererProvider.Context context;
    private ExplosionFuelGeneratorModel model;
    private ResourceLocation TEXTURE = new ResourceLocation(Main.MOD_ID, "textures/block/explosion_fuel_generator.png");

    public ExplosionFuelGeneratorRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.model = new ExplosionFuelGeneratorModel(this.context.bakeLayer(ModModelLayers.EXPLOSION_FUEL_GENERATOR));
    }

    @Override
    public void render(@NotNull T pBlockEntity, float pPartialTick, PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(new Quaternionf().rotateXYZ(
                (float) Math.toRadians(180 %360),
                (float) Math.toRadians(180 % 360),
                (float) Math.toRadians(0 % 360)));
        pPoseStack.translate(-8 / 16F,-24 / 16F,8 / 16F);
        Direction blockdir = pBlockEntity.getBlockState().getValue(ExplosionFuelGeneratorBlock.FACING);
        pPoseStack.mulPose(new Quaternionf().rotateXYZ(
                (float) Math.toRadians(0 % 360),
                (float) Math.toRadians(blockdir.toYRot() % 360),
                (float) Math.toRadians(0 % 360)
        ));
        this.model.setupAnim((ExplosionFuelGeneratorBlockEntity) pBlockEntity,
                ((ExplosionFuelGeneratorBlockEntity) pBlockEntity).tickCount + pPartialTick);
        this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(this.model.renderType(TEXTURE)), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        pPoseStack.pushPose();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = ((ExplosionFuelGeneratorBlockEntity) pBlockEntity).getRenderStack();
        BakedModel itemModel = itemRenderer.getModel(itemStack, pBlockEntity.getLevel(), null, 1);
        switch (blockdir.get2DDataValue()) {
            case 0 -> pPoseStack.translate(0.37, 0.41, 0.5);
            case 1 -> {
                pPoseStack.translate(0.5, 0.41, 0.37);
                pPoseStack.mulPose(new Quaternionf().rotateXYZ(
                        (float) Math.toRadians(0 % 360),
                        (float) Math.toRadians(270 % 360),
                        (float) Math.toRadians(0 % 360)
                ));
            }
            case 2 -> pPoseStack.translate(0.63, 0.41, 0.5);
            case 3 -> {
                pPoseStack.translate(0.5, 0.41, 0.63);
                pPoseStack.mulPose(new Quaternionf().rotateXYZ(
                        (float) Math.toRadians(0 % 360),
                        (float) Math.toRadians(90 % 360),
                        (float) Math.toRadians(0 % 360)
                ));
            }
            default -> pPoseStack.translate(0,0,0);
        }
        pPoseStack.scale(0.25F,0.25F,0.25F);
        itemRenderer.render(itemStack, ItemDisplayContext.GROUND , true, pPoseStack, pBuffer, getLightLevel(pBlockEntity.getLevel(),
                pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, itemModel);
        pPoseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
