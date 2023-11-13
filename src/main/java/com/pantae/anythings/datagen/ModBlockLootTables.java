package com.pantae.anythings.datagen;

import com.pantae.anythings.Main;
import com.pantae.anythings.block.ModBlocks;
import com.pantae.anythings.enums.ModBlockEnums;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    protected ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        Main.BLOCKS.MAP.forEach((key, value) -> {
            dropSelf(value.get());
        });
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return Main.BLOCKS.MAP.values().stream().map(RegistryObject::get)::iterator;
    }
}
