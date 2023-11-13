package com.pantae.anythings.entity.custom;

import com.pantae.anythings.Main;
import com.pantae.anythings.enums.ModEntityEnums;
import com.pantae.anythings.helper.ExplosionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExplodingProjectile extends Projectile {
    private boolean inGround = false;
    private LivingEntity shooter;
    private boolean shooting = false;
    public ExplodingProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static ExplodingProjectile shoot(LivingEntity pShooter, float pX,
                                            float pY,
                                            float pZ,
                                            float pVelocity,
                                            float pInaccuracy, Vec3 shootPos, Level pLevel) {
        ExplodingProjectile instance = new ExplodingProjectile((EntityType<? extends Projectile>) Main.ENTITIES.getRegistry(ModEntityEnums.EXPLODING_PROJECTILE).get(), pLevel);
        instance.shooter = pShooter;
        instance.setPos(shootPos);
        instance.shooting = true;
        instance.shootFromRotation(pShooter, pX, pY, pZ, pVelocity, pInaccuracy);
        return instance;
    }
    @Override
    protected void defineSynchedData() {

    }

    public static Entity getNearestEntity(Level level, double x, double y, double z) {
        AABB aabb = new AABB(x - 2, y - 2, z - 2, x + 2, y + 2, z + 2);
        return level.getNearestEntity(LivingEntity.class, TargetingConditions.DEFAULT, null,x,y,z, aabb);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount >= 10) this.shooting = false;
        Entity nearestEntity = getNearestEntity(level(), getX(), getY(), getZ());
        if (nearestEntity != null && this.shooter != null) {
            if (nearestEntity != this.shooter && shooting && nearestEntity.position().closerThan(this.position(), 3f)) {
                this.inGround = true;
            }
            if (!shooting && nearestEntity.position().closerThan(this.position(), 3f)) {
                this.inGround = true;
            }
        }
        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level().getBlockState(blockpos);
        if (!blockstate.isAir()) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.level(), blockpos);
            if (!voxelshape.isEmpty()) {
                Vec3 vec31 = this.position();

                for (AABB aabb : voxelshape.toAabbs()) {
                    if (aabb.move(blockpos).contains(vec31)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }
        if (this.inGround) {
            ExplosionHelper.FallingBlockExplosion(this.blockPosition(), this.level(), 4, 16,
                            this.damageSources().explosion(shooter, shooter), null);
            this.discard();
        } else {
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = vec3.horizontalDistance();
            this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI)));
            this.setXRot((float) (Mth.atan2(vec3.y, d0) * (double) (180F / (float) Math.PI)));
            float f = 0.99F;
            this.setDeltaMovement(vec3.scale((double) f));
            if (!this.isNoGravity()) {
                Vec3 vec34 = this.getDeltaMovement();
                this.setDeltaMovement(vec34.x, vec34.y - (double) 0.05F, vec34.z);
            }
            vec3 = this.getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;
            double d7 = this.getX() + d5;
            double d2 = this.getY() + d6;
            double d3 = this.getZ() + d1;
            this.setPos(d7, d2, d3);
        }
    }
}
