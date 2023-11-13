package com.pantae.anythings.block.custom;

import com.pantae.anythings.Main;
import com.pantae.anythings.block.CTBBlock;
import com.pantae.anythings.enums.ModSoundEnums;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ForgeSoundType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class ReactorCasing extends CTBBlock {
    public ReactorCasing() {
        super(Properties.of().requiresCorrectToolForDrops().strength(4));
    }
}
