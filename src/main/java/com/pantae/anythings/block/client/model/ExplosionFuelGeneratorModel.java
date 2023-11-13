package com.pantae.anythings.block.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pantae.anythings.Animations;
import com.pantae.anythings.block.entity.ExplosionFuelGeneratorBlockEntity;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.metadata.animation.AnimationFrame;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ExplosionFuelGeneratorModel extends Model {
    private final ModelPart explosion_fuel_generator;
    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

    public ExplosionFuelGeneratorModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.explosion_fuel_generator = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition explosion_fuel_generator = partdefinition.addOrReplaceChild("explosion_fuel_generator", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition explosion_chember = explosion_fuel_generator.addOrReplaceChild("explosion_chember", CubeListBuilder.create().texOffs(22, 38).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(13, 38).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(9, 12).addBox(-1.0F, -9.0F, 1.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(11, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(9, 24).addBox(1.0F, -9.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(9, 19).addBox(-1.0F, -9.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.0F, 0.0F));

        PartDefinition piston = explosion_fuel_generator.addOrReplaceChild("piston", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -9.0F, -2.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = piston.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 38).addBox(0.0F, -9.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(9, 9).addBox(-2.0F, -7.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = explosion_fuel_generator.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 19).addBox(-8.0F, -2.0F, -8.0F, 16.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(59, 38).addBox(-8.0F, -14.0F, 6.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(50, 53).addBox(6.0F, -14.0F, 6.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(50, 38).addBox(6.0F, -14.0F, -8.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 19).addBox(-8.0F, -14.0F, -8.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition window1 = body.addOrReplaceChild("window1", CubeListBuilder.create().texOffs(49, 0).addBox(-6.0F, -14.0F, -8.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition window2 = body.addOrReplaceChild("window2", CubeListBuilder.create().texOffs(0, 38).addBox(8.0F, -14.0F, -6.0F, 0.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition window3 = body.addOrReplaceChild("window3", CubeListBuilder.create().texOffs(25, 38).addBox(-8.0F, -14.0F, -6.0F, 0.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition window4 = body.addOrReplaceChild("window4", CubeListBuilder.create().texOffs(49, 19).addBox(0.0F, -7.0F, 0.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -7.0F, 8.0F));

        PartDefinition battery = explosion_fuel_generator.addOrReplaceChild("battery", CubeListBuilder.create().texOffs(0, 9).addBox(-6.0F, -6.0F, 4.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 80, 80);
    }

    public void setupAnim(ExplosionFuelGeneratorBlockEntity blockEntity, float ageInTicks) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animate(blockEntity.running, Animations.RUNNING, ageInTicks, 1f);
        this.animate(blockEntity.open, Animations.OPEN, ageInTicks, 1f);
        this.animate(blockEntity.close, Animations.CLOSE, ageInTicks, 1f);
    }

    protected void animate(AnimationState pAnimationState, AnimationDefinition pAnimationDefinition, float pAgeInTicks, float pSpeed) {
        pAnimationState.updateTime(pAgeInTicks, pSpeed);
        pAnimationState.ifStarted((p_233392_) -> {
            ExplosionFuelGeneratorModel.animate(this, pAnimationDefinition, p_233392_.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
        });
    }

    private static float getElapsedSeconds(AnimationDefinition pAnimationDefinition, long pAccumulatedTime) {
        float f = (float)pAccumulatedTime / 1000.0F;
        return pAnimationDefinition.looping() ? f % pAnimationDefinition.lengthInSeconds() : f;
    }

    public static void animate(ExplosionFuelGeneratorModel pModel, AnimationDefinition pAnimationDefinition, long pAccumulatedTime, float pScale, Vector3f pAnimationVecCache) {
        float f = getElapsedSeconds(pAnimationDefinition, pAccumulatedTime);

        for(Map.Entry<String, List<AnimationChannel>> entry : pAnimationDefinition.boneAnimations().entrySet()) {
            Optional<ModelPart> optional = pModel.getAnyDescendantWithName(entry.getKey());
            List<AnimationChannel> list = entry.getValue();
            optional.ifPresent((p_232330_) -> {
                list.forEach((p_288241_) -> {
                    Keyframe[] akeyframe = p_288241_.keyframes();
                    int i = Math.max(0, Mth.binarySearch(0, akeyframe.length, (p_232315_) -> {
                        return f <= akeyframe[p_232315_].timestamp();
                    }) - 1);
                    int j = Math.min(akeyframe.length - 1, i + 1);
                    Keyframe keyframe = akeyframe[i];
                    Keyframe keyframe1 = akeyframe[j];
                    float f1 = f - keyframe.timestamp();
                    float f2;
                    if (j != i) {
                        f2 = Mth.clamp(f1 / (keyframe1.timestamp() - keyframe.timestamp()), 0.0F, 1.0F);
                    } else {
                        f2 = 0.0F;
                    }

                    keyframe1.interpolation().apply(pAnimationVecCache, f2, akeyframe, i, j, pScale);
                    p_288241_.target().apply(p_232330_, pAnimationVecCache);
                });
            });
        }
    }

    public Optional<ModelPart> getAnyDescendantWithName(String pName) {
        return this.root().getAllParts().filter((p_233400_) -> p_233400_.hasChild(pName)).findFirst().map((p_233397_) -> p_233397_.getChild(pName));
    }

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        explosion_fuel_generator.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }

    public ModelPart root() {
        return this.explosion_fuel_generator;
    }
}
