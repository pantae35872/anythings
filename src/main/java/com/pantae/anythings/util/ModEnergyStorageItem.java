package com.pantae.anythings.util;

import com.pantae.anythings.Main;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import javax.naming.CompoundName;

public class ModEnergyStorageItem extends ModEnergyStorage{
    private final ItemStack stack;
    public ModEnergyStorageItem(ItemStack stack, int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
        this.stack = stack;
        if (this.stack.getTag() == null) this.stack.setTag(new CompoundTag());
        this.setEnergy(this.stack.getTag().getInt("energy"));
    }

    @Override
    public void onEnergyChanged() {
        if (this.stack.getTag() != null) {
            this.stack.getTag().putInt("energy", energy);
        }
    }
}
