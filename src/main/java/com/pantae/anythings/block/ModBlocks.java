package com.pantae.anythings.block;

import com.pantae.anythings.Main;
import com.pantae.anythings.abstracts.ModRegistries;
import com.pantae.anythings.block.custom.ExplosionFuelGeneratorBlock;
import com.pantae.anythings.block.custom.ReactorCasing;
import com.pantae.anythings.enums.ModBlockEnums;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class ModBlocks extends ModRegistries<Block, ModBlockEnums> {
    public static final DeferredRegister<Block>BLOCK = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MOD_ID);

    public HashMap<ModBlockEnums, RegistryObject<Item>> BLOCK_ITEM_MAP = new HashMap<>();
    public ModBlocks() {
        super(BLOCK);
        for (Map.Entry<ModBlockEnums, RegistryObject<Block>> entry : MAP.entrySet()) {
            BLOCK_ITEM_MAP.put(entry.getKey(), Main.ITEMS.register(entry.getValue().getId().getPath(), () -> new BlockItem(entry.getValue().get(), new Item.Properties())));
        }
    }
    @Override
    protected void initializeRegistries() {
        MAP.put(ModBlockEnums.REACTOR_CASING, this.register("reactor_casing", ReactorCasing::new));
        MAP.put(ModBlockEnums.EXPLOSION_FUEL_GENERATOR, this.register("explosion_fuel_generator", ExplosionFuelGeneratorBlock::new));
    }
}
