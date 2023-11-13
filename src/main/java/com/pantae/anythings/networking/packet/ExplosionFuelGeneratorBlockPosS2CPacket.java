package com.pantae.anythings.networking.packet;

import com.pantae.anythings.Main;
import com.pantae.anythings.block.entity.ExplosionFuelGeneratorBlockEntity;
import com.pantae.anythings.enums.ModPacketS2CEnum;
import com.pantae.anythings.networking.ModPacket;
import com.pantae.anythings.networking.PendingPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ExplosionFuelGeneratorBlockPosS2CPacket extends ModPacket {
    public BlockPos blockPos;
    public ExplosionFuelGeneratorBlockPosS2CPacket(BlockPos blockPos) {
        super(new FriendlyByteBuf(ByteBufAllocator.DEFAULT.buffer()).writeBlockPos(blockPos));
        this.blockPos = blockPos;
    }

    public ExplosionFuelGeneratorBlockPosS2CPacket(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
    }

    @Override
    public void receivePacket(CustomPayloadEvent.Context context) {
        PendingPacket.addS2C(ModPacketS2CEnum.ExplosionFuelGeneratorBlockPosS2C, this);
    }
}
