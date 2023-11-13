package com.pantae.anythings.entity;

import com.pantae.anythings.Main;
import com.pantae.anythings.abstracts.ModRegistries;
import com.pantae.anythings.entity.custom.ExplodingProjectile;
import com.pantae.anythings.enums.ModEntityEnums;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities extends ModRegistries<EntityType<?>, ModEntityEnums> {
    private static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Main.MOD_ID);
    public ModEntities() {
        super(ENTITY);
    }

    @Override
    protected void initializeRegistries() {
        this.MAP.put(ModEntityEnums.EXPLODING_PROJECTILE, this.register("exploding_projectile", () -> EntityType.Builder.of(
                ExplodingProjectile::new, MobCategory.MISC
        ).sized(1.2f,1.2f).build("exploding_projectile")));
    }
}
