package com.pantae.anythings.networking.packet;

import com.pantae.anythings.Main;
import com.pantae.anythings.networking.ModNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class TestS2CPacket {
    private final String msg;

    public TestS2CPacket(String msg) {
        this.msg = msg;
    }

    public TestS2CPacket(FriendlyByteBuf buf) {
        this.msg = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(msg);
    }

    public void receivePacket(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            Main.LOGGER.debug(msg);
            ModNetwork.sendToServer(new TestC2SPacket("what!"));
        });
    }
}
