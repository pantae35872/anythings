package com.pantae.anythings;

import com.pantae.anythings.Main;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation EXPLODINGPROJECTILE = new ModelLayerLocation(
            new ResourceLocation(Main.MOD_ID, "exploding_projectile_layer"), "main"
    );

    public static final ModelLayerLocation EXPLOSION_FUEL_GENERATOR = new ModelLayerLocation(
            new ResourceLocation(Main.MOD_ID, "explosion_fuel_generator_layer"), "main"
    );
}
