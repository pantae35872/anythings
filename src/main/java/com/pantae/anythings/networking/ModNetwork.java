package com.pantae.anythings.networking;

import com.pantae.anythings.Main;
import com.pantae.anythings.block.custom.ExplosionFuelGeneratorBlock;
import com.pantae.anythings.networking.packet.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

import java.util.Objects;

public class ModNetwork {
    private static final SimpleChannel CHANNEL = ChannelBuilder.named(new ResourceLocation(Main.MOD_ID, "messages")).simpleChannel();
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        CHANNEL.messageBuilder(TestS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(TestS2CPacket::new)
                .encoder(TestS2CPacket::encode)
                .consumerMainThread(TestS2CPacket::receivePacket)
                .add();

        CHANNEL.messageBuilder(TestC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(TestC2SPacket::new)
                .encoder(TestC2SPacket::encode)
                .consumerMainThread(TestC2SPacket::receivePacket)
                .add();

        CHANNEL.messageBuilder(ExplosionFuelGeneratorBlockPosS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ExplosionFuelGeneratorBlockPosS2CPacket::new)
                .encoder(ExplosionFuelGeneratorBlockPosS2CPacket::encode)
                .consumerMainThread(ExplosionFuelGeneratorBlockPosS2CPacket::receive)
                .add();

        CHANNEL.messageBuilder(ExplosionFuelGeneratorSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ExplosionFuelGeneratorSyncS2CPacket::new)
                .encoder(ExplosionFuelGeneratorSyncS2CPacket::toBytes)
                .consumerMainThread(ExplosionFuelGeneratorSyncS2CPacket::handle)
                .add();

        CHANNEL.messageBuilder(ExplosionFuelGeneratorAnimationSyncS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ExplosionFuelGeneratorAnimationSyncS2C::new)
                .encoder(ExplosionFuelGeneratorAnimationSyncS2C::encode)
                .consumerMainThread(ExplosionFuelGeneratorAnimationSyncS2C::receive)
                .add();

        CHANNEL.messageBuilder(ExplosionFuelGeneratorOpenCloseS2CSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ExplosionFuelGeneratorOpenCloseS2CSyncPacket::new)
                .encoder(ExplosionFuelGeneratorOpenCloseS2CSyncPacket::toBytes)
                .consumerMainThread(ExplosionFuelGeneratorOpenCloseS2CSyncPacket::handle)
                .add();

        CHANNEL.messageBuilder(ExplosionFuelGeneratorScreenStateC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ExplosionFuelGeneratorScreenStateC2SPacket::new)
                .encoder(ExplosionFuelGeneratorScreenStateC2SPacket::toBytes)
                .consumerMainThread(ExplosionFuelGeneratorScreenStateC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToClient (MSG packet, PacketDistributor.PacketTarget packetTarget) {
        CHANNEL.send(packet, packetTarget);
    }

    public static <MSG> void sendToServer (MSG packet) {
        CHANNEL.send(packet, Objects.requireNonNull(Minecraft.getInstance().getConnection()).getConnection());
    }
}
