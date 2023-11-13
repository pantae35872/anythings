package com.pantae.anythings.networking.packet;

import com.pantae.anythings.Main;
import com.pantae.anythings.block.entity.ExplosionFuelGeneratorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ExplosionFuelGeneratorOpenCloseS2CSyncPacket {
    private final BlockPos pos;
    private final boolean open;
    private final boolean close;

    public ExplosionFuelGeneratorOpenCloseS2CSyncPacket(BlockPos pos, boolean open, boolean close) {
        this.pos = pos;
        this.open = open;
        this.close = close;
    }

    public ExplosionFuelGeneratorOpenCloseS2CSyncPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.open = buf.readBoolean();
        this.close = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(open);
        buf.writeBoolean(close);
    }

    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof ExplosionFuelGeneratorBlockEntity blockEntity) {
                if (open && close) Main.LOGGER.error("Illegal open-close state from " + blockEntity.getBlockPos());
                else if (open) {
                    blockEntity.open.startIfStopped(blockEntity.tickCount);
                    blockEntity.close.stop();
                } else if (close) {
                    blockEntity.close.startIfStopped(blockEntity.tickCount);
                    blockEntity.open.stop();
                }
            }
        });
    }
}
