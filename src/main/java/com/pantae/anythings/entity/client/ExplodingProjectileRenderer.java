package com.pantae.anythings.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.pantae.anythings.Main;
import com.pantae.anythings.ModModelLayers;
import com.pantae.anythings.entity.custom.ExplodingProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ExplodingProjectileRenderer<T extends Entity> extends EntityRenderer<T> {
    private ExplodingProjectileModel<ExplodingProjectile> model;
    public ExplodingProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        model = new ExplodingProjectileModel<ExplodingProjectile>(pContext.bakeLayer(ModModelLayers.EXPLODINGPROJECTILE));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T pEntity) {
        return new ResourceLocation(Main.MOD_ID, "textures/entity/exploding_projectile.png");
    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        RenderType rendertype = this.model.renderType(getTextureLocation(pEntity));
        VertexConsumer vertexconsumer = pBuffer.getBuffer(rendertype);
        double yRotation = Math.toRadians(pEntity.getYRot());
        double xRotation = Math.toRadians(pEntity.getXRot());
        double xOffset = -Math.sin(yRotation) * Math.cos(xRotation);
        double yOffset = -Math.sin(xRotation) + 0.2;
        double zOffset = -Math.cos(yRotation) * Math.cos(xRotation);
        pPoseStack.scale(2,2,2);
        pPoseStack.translate(xOffset,yOffset, zOffset);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.xRotO, pEntity.getXRot()) - 90.0F));
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, getOverlay(), 1.0F, 1.0F, 1.0F, 0.15F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    private static int getOverlay() {
        return OverlayTexture.pack(OverlayTexture.u(0), OverlayTexture.v(false));
    }
}
