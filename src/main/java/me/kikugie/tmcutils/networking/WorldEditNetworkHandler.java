package me.kikugie.tmcutils.networking;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.kikugie.tmcutils.TMCUtilsMod;
import me.kikugie.tmcutils.features.WorldEditSync;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;

public class WorldEditNetworkHandler {
    private static final Identifier CHANNEL = new Identifier("worldedit:cui");
    private static final int PROTOCOL = 4;
    private static boolean worldEditConnected = false;

    public static boolean isWorldEditConnected() {
        return worldEditConnected;
    }

    public static void registerReceiver() {
        worldEditConnected = false;
        ClientPlayNetworking.registerReceiver(CHANNEL, WorldEditNetworkHandler::onPacket);
        handshake();
    }

    private static void onPacket(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf data, PacketSender packetSender) {
        if (!worldEditConnected) {
            worldEditConnected = true;
            TMCUtilsMod.LOGGER.info("WorldEdit connected");

            WorldEditSync.onWorldEditConnected();
        }
    }

    private static void handshake() {
        String message = "v|" + PROTOCOL;
        ByteBuf buf = Unpooled.copiedBuffer(message, StandardCharsets.UTF_8);
        ClientPlayNetworking.send(CHANNEL, new PacketByteBuf(buf));
    }
}
