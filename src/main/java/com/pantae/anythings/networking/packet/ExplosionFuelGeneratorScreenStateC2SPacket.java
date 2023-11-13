package com.pantae.anythings.networking.packet;

import com.pantae.anythings.Main;
import com.pantae.anythings.block.entity.ExplosionFuelGeneratorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.Map;
import java.util.UUID;

public class ExplosionFuelGeneratorScreenStateC2SPacket {
    private final BlockPos pos;
    private final boolean open;
    private final UUID uuid;

    public ExplosionFuelGeneratorScreenStateC2SPacket(BlockPos pos, boolean open, UUID player) {
        this.pos = pos;
        this.open = open;
        this.uuid = player;
    }

    public ExplosionFuelGeneratorScreenStateC2SPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.open = buf.readBoolean();
        this.uuid = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(open);
        buf.writeUUID(uuid);
    }

    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if(context.getSender().level().getBlockEntity(pos) instanceof ExplosionFuelGeneratorBlockEntity blockEntity) {
                blockEntity.addScreenState(open, uuid);
            }
        });
    }
}
