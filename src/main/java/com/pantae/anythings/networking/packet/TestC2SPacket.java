package com.pantae.anythings.networking.packet;

import com.pantae.anythings.Main;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class TestC2SPacket {
    private final String msg;

    public TestC2SPacket(String msg) {
        this.msg = msg;
    }

    public TestC2SPacket(FriendlyByteBuf buf) {
        this.msg = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(msg);
    }

    public void receivePacket(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            Main.LOGGER.debug(msg);
        });
    }
}
