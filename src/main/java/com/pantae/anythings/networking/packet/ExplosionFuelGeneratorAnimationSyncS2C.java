package com.pantae.anythings.networking.packet;

import com.pantae.anythings.block.entity.ExplosionFuelGeneratorBlockEntity;
import com.pantae.anythings.networking.ModPacket;
import io.netty.buffer.ByteBufAllocator;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ExplosionFuelGeneratorAnimationSyncS2C extends ModPacket {
    private boolean is_running;
    private BlockPos pos;

    public ExplosionFuelGeneratorAnimationSyncS2C(FriendlyByteBuf buf) {
        super(buf);
    }

    public ExplosionFuelGeneratorAnimationSyncS2C(boolean is_running, BlockPos pos) {
        super(new FriendlyByteBuf(ByteBufAllocator.DEFAULT.buffer()).writeBoolean(is_running).writeBlockPos(pos));
        this.is_running = is_running;
        this.pos = pos;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.is_running);
        buf.writeBlockPos(this.pos);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.is_running = buf.readBoolean();
        this.pos = buf.readBlockPos();
    }

    @Override
    public void receivePacket(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof ExplosionFuelGeneratorBlockEntity blockEntity) {
                if (is_running) { blockEntity.running.startIfStopped(blockEntity.tickCount);  } else blockEntity.running.stop();
            }
        });
    }
}
