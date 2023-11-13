package com.pantae.anythings.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pantae.anythings.entity.custom.ExplodingProjectile;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.MobRenderer;

public class ExplodingProjectileModel<T extends ExplodingProjectile> extends HierarchicalModel<T> {
    private final ModelPart exploding_projectile;

    public ExplodingProjectileModel(ModelPart root) {
        this.exploding_projectile = root.getChild("exploding_projectile");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition exploding_projectile = partdefinition.addOrReplaceChild("exploding_projectile", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = exploding_projectile.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -14.0F, -1.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(23, 0).addBox(-1.0F, -14.0F, -3.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 14).addBox(-1.0F, -14.0F, 1.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(7, 16).addBox(1.0F, -14.0F, -1.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(14, 30).addBox(1.0F, -13.0F, -3.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 30).addBox(1.0F, -13.0F, 1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(1.0F, -5.0F, 1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 5).addBox(-2.0F, -5.0F, 1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 0).addBox(-2.0F, -5.0F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 17).addBox(1.0F, -5.0F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(25, 27).addBox(-3.0F, -13.0F, 1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(25, 16).addBox(-3.0F, -13.0F, -3.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(9, 0).addBox(-3.0F, -14.0F, -1.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 48, 48);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        exploding_projectile.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.exploding_projectile;
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

    }
}
