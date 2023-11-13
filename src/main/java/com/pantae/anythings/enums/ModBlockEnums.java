package com.pantae.anythings.enums;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public enum ModBlockEnums {
    REACTOR_CASING(BlockTags.NEEDS_IRON_TOOL, BlockTags.MINEABLE_WITH_PICKAXE, false),
    EXPLOSION_FUEL_GENERATOR(BlockTags.NEEDS_IRON_TOOL, BlockTags.MINEABLE_WITH_PICKAXE, false);
    private final TagKey<Block> need_tool;
    private final TagKey<Block> mineable;
    private final boolean ender_man_holdable;

    ModBlockEnums(TagKey<Block> need_tool, TagKey<Block> mineable, boolean enderman_holdable) {
        this.need_tool = need_tool;
        this.mineable = mineable;
        this.ender_man_holdable = enderman_holdable;
    }
    ModBlockEnums(TagKey<Block> need_tool, TagKey<Block> mineable) {
        this.need_tool = need_tool;
        this.mineable = mineable;
        this.ender_man_holdable = false;
    }

    public TagKey<Block> getNeed_tool() {
        return this.need_tool;
    }

    public TagKey<Block> getMineable() {
        return this.mineable;
    }

    public boolean getEnderManHoldable() {
        return this.ender_man_holdable;
    }
}
