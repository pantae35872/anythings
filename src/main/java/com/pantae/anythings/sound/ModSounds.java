package com.pantae.anythings.sound;

import com.pantae.anythings.Main;
import com.pantae.anythings.abstracts.ModRegistries;
import com.pantae.anythings.enums.ModSoundEnums;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds extends ModRegistries<SoundEvent, ModSoundEnums> {
    private static final DeferredRegister<SoundEvent> SOUND = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS
    , Main.MOD_ID);
    public ModSounds() {
        super(SOUND);
    }

    public RegistryObject<SoundEvent> register(String name) {
        return super.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Main.MOD_ID, name)));
    }

    @Override
    protected void initializeRegistries() {
    }
}
