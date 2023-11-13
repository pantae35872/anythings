package com.pantae.anythings.networking.packet;

import com.pantae.anythings.Main;
import com.pantae.anythings.block.entity.ExplosionFuelGeneratorBlockEntity;
import com.pantae.anythings.screen.ExplosionFuelGeneratorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ExplosionFuelGeneratorSyncS2CPacket {
    private final int energy;
    private final BlockPos pos;
    private final ItemStack renderItem;

    public ExplosionFuelGeneratorSyncS2CPacket(int energy, BlockPos pos, ItemStack renderItem) {
        this.energy = energy;
        this.pos = pos;
        this.renderItem = renderItem;
    }

    public ExplosionFuelGeneratorSyncS2CPacket(FriendlyByteBuf buf) {
        this.energy = buf.readInt();
        this.pos = buf.readBlockPos();
        this.renderItem = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(energy);
        buf.writeBlockPos(pos);
        buf.writeItem(renderItem);
    }

    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof ExplosionFuelGeneratorBlockEntity blockEntity) {
                blockEntity.setEnergyLevel(energy);
                blockEntity.setLatestFuel(renderItem);
                if(Minecraft.getInstance().player.containerMenu instanceof ExplosionFuelGeneratorMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    blockEntity.setEnergyLevel(energy);
                }
            }
        });
    }
}
