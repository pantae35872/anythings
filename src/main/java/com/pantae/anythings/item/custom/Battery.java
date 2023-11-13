package com.pantae.anythings.item.custom;

import com.pantae.anythings.Main;
import com.pantae.anythings.util.ModEnergyStorage;
import com.pantae.anythings.util.ModEnergyStorageItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Battery extends Item {

    public Battery() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (nbt != null) {
            if (!nbt.contains("energy")) {
                nbt.putInt("energy", 0);
            }
        }
        return new ICapabilityProvider() {
            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                if (cap == ForgeCapabilities.ENERGY) {
                    if (nbt != null) {
                        return LazyOptional.of(() -> new ModEnergyStorageItem(stack, 1000, 10)).cast();
                    }
                }
                return LazyOptional.empty();
            }
        };
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.getCapability(ForgeCapabilities.ENERGY).resolve().isPresent()) {
            pTooltipComponents.add(Component.literal(pStack.getCapability(ForgeCapabilities.ENERGY).resolve().get().getEnergyStored() + "/" + pStack.getCapability(ForgeCapabilities.ENERGY).resolve().get().getMaxEnergyStored() + " FE"));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
