package com.pantae.anythings.datagen;

import com.pantae.anythings.Main;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModTagsProvider extends BlockTagsProvider {

    public ModTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        Main.BLOCKS.MAP.forEach((key, value) -> {
            tag(key.getNeed_tool()).add(value.get());
            tag(key.getMineable()).add(value.get());
            if (key.getEnderManHoldable()) {
                tag(BlockTags.ENDERMAN_HOLDABLE).add(value.get());
            }
        });
    }
}
