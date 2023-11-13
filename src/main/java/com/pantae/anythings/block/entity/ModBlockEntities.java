package com.pantae.anythings.block.entity;

import com.pantae.anythings.Main;
import com.pantae.anythings.abstracts.ModRegistries;
import com.pantae.anythings.block.ModBlocks;
import com.pantae.anythings.enums.ModBlockEntityEnum;
import com.pantae.anythings.enums.ModBlockEnums;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlockEntities extends ModRegistries<BlockEntityType<?>, ModBlockEntityEnum> {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MOD_ID);
    public ModBlockEntities() {
        super(BLOCK_ENTITY);
    }

    @Override
    protected void initializeRegistries() {
        MAP.put(ModBlockEntityEnum.ExplosionFuelGenerator, this.register("explosion_fuel_generator", () ->
                BlockEntityType.Builder.of(ExplosionFuelGeneratorBlockEntity::new, Main.BLOCKS.getRegistry(ModBlockEnums.EXPLOSION_FUEL_GENERATOR).get())
                        .build(null)));
    }
}
