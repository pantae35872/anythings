package com.pantae.anythings.datagen;

import com.pantae.anythings.Main;
import com.pantae.anythings.sound.ModSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class ModSoundProvider extends SoundDefinitionsProvider {
    protected ModSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Main.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        Main.SOUNDS.MAP.forEach((key, value) ->
                {
                    this.add(value.get(), definition()
                            .subtitle(subtitleSoundCalculator(value.get()))
                            .with(
                                    sound(soundCalculator(value.get()))
                                            .weight(key.getWeight())
                                            .volume(key.getVolume())
                            ));
                }
        );
    }

    private String subtitleSoundCalculator(SoundEvent soundEvent) {
        return "sounds." + Main.MOD_ID + "." + soundEvent.getLocation().getPath();
    }

    private ResourceLocation soundCalculator(SoundEvent soundEvent) {
        return soundEvent.getLocation();
    }
}
