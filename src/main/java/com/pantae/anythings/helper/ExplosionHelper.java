package com.pantae.anythings.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ExplosionHelper {
    public static void FallingBlockExplosion(BlockPos pPos, Level pLevel, int range, int explosion_power, @Nullable DamageSource damageSource,
                                             @Nullable LivingEntity source) {
        for (int x = pPos.getX() - range; x < pPos.getX() + range; x++) {
            for (int y = pPos.getY() - range; y < pPos.getY() + range; y++) {
                for (int z = pPos.getZ() -range; z < pPos.getZ() + range; z++) {
                    BlockPos des_pos = new BlockPos(x, y, z);
                    BlockState state = pLevel.getBlockState(des_pos);
                    float f = explosion_power * (0.7F + pLevel.random.nextFloat() * 0.6F);
                    f -= (state.getExplosionResistance(null,null,null) + 0.3F) * 0.3F;
                    if (state.getExplosionResistance(null,null,null) <= f) {
                        pLevel.removeBlock(des_pos, true);
                        FallingBlockEntity fallingBlock = FallingBlockEntity.fall(pLevel, des_pos, state);
                        fallingBlock.dropItem = false;
                        range = 1;
                        float xx = (float) -range + (float) (Math.random() * ((range - -range) + 1));
                        float yy = (float) -range + (float) (Math.random() * ((range - -range) + 1));
                        float zz = (float) -range + (float) (Math.random() * ((range - -range) + 1));
                        fallingBlock.setDeltaMovement(new Vec3(xx, yy, zz));
                        range = 4;
                    }
                }
            }
        }
        Explosion ex = pLevel.explode(source, damageSource, null, pPos.getX(), pPos.getY(), pPos.getZ(), explosion_power,true, Level.ExplosionInteraction.BLOCK);
    }

    public static void FallingBlockExplosion(BlockPos pPos, Level pLevel, int range, int explosion_power) {
        FallingBlockExplosion(pPos, pLevel, range, explosion_power, null, null);
    }
}
