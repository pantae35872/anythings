package com.pantae.anythings.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pantae.anythings.Main;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.FurnaceRecipeFix;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExplosionFuelGeneratorSerializer <T extends ExplosionFuelGeneratorRecipe> implements RecipeSerializer<T> {
    public static final ResourceLocation ID = new ResourceLocation(Main.MOD_ID, "explosion_fuel_generator");
    public Codec<T> codec;
    public SerializerBaker<T> factory;

    public ExplosionFuelGeneratorSerializer(SerializerBaker<T> factory) {
        this.factory = factory;
        this.codec = RecordCodecBuilder.create((ins) -> {
            return ins.group(
                    Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter((receipe) -> {
                        return receipe.recipeItems;
                    }),
                    Codec.INT.listOf().listOf().fieldOf("outputs").forGetter(power -> {
                        return power.output;
                    })
            ).apply(ins, factory::create);
        });
    }

    public Codec<T> codec() {
        return  this.codec;
    }

    @Override
    public @Nullable T fromNetwork(FriendlyByteBuf byteBuf) {
        int recipeItemsLength = byteBuf.readInt();
        int output1Length = byteBuf.readInt();
        NonNullList<Ingredient> recipeItems = NonNullList.withSize(recipeItemsLength, Ingredient.EMPTY);
        NonNullList<List<Integer>> outputs = NonNullList.withSize(output1Length, new ArrayList<>());
        for (int i = 0; i < recipeItemsLength; i++) {
            recipeItems.set(i, Ingredient.fromNetwork(byteBuf));
        }
        for (int x = 0; x < output1Length; x++) {
            List<Integer> val = new ArrayList<>();
            for (int y = 0; y < 2; y++) {
                val.add(y, byteBuf.readInt());
            }
            outputs.set(x, val);
        }
        return this.factory.create(recipeItems, outputs);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, ExplosionFuelGeneratorRecipe pRecipe) {
        pBuffer.writeInt(pRecipe.recipeItems.size());
        pBuffer.writeInt(pRecipe.output.size());
        pRecipe.recipeItems.forEach((ingredient -> ingredient.toNetwork(pBuffer)));
        pRecipe.output.forEach(list -> list.forEach(pBuffer::writeInt));
    }

    interface SerializerBaker<T extends ExplosionFuelGeneratorRecipe> {
        T create(List<Ingredient> recipeItems, List<List<Integer>> output);
    }
}
