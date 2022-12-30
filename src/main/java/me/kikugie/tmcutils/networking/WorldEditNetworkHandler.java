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
import net.minecraft.util.math.BlockPos;

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
        WorldEditStorage.reset();
        ClientPlayNetworking.registerReceiver(CHANNEL, WorldEditNetworkHandler::onPacket);
        handshake();
    }

    private static void onPacket(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf data, PacketSender packetSender) {
        if (!worldEditConnected) {
            worldEditConnected = true;
            TMCUtilsMod.LOGGER.info("WorldEdit connected");
            WorldEditSync.onWorldEditConnected();
        }
        int bytes = data.readableBytes();
        if (bytes == 0) {
            TMCUtilsMod.LOGGER.warn("WorldEditNetworkHandler#onPacket(): Received CUI packet of length zero");
            return;
        }
        String message = data.toString(0, data.readableBytes(), StandardCharsets.UTF_8);
        String[] split = message.split("\\|", -1);
        boolean multi = split[0].startsWith("+");
        String type = split[0].substring(multi ? 1 : 0);
        String[] args = message.substring(type.length() + (multi ? 2 : 1)).split("\\|", -1);
        TMCUtilsMod.LOGGER.info(message);
        handlePacket(type, args);
    }

    private static void handlePacket(String type, String[] args) {
        if (type.equals("s")) {
            WorldEditStorage.reset();
            WorldEditStorage.mode = args[0];
        } else if (type.equals("p")) {
            if (!WorldEditStorage.mode.equals("cuboid")) {
                TMCUtilsMod.LOGGER.debug("WorldEditNetworkHandler#handlePacket(): Received position for unsupported mode: " + WorldEditStorage.mode);
                return;
            }
            try {
                int posId = Integer.parseInt(args[0]);
                BlockPos pos = new BlockPos(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                WorldEditStorage.setPos(posId, pos);
            } catch (NumberFormatException e) {
                TMCUtilsMod.LOGGER.warn("WorldEditCUINetworkHandler#handlePackett(): Failed int parsing of position");
                e.printStackTrace();
            }
        }
    }

    private static void handshake() {
        String message = "v|" + PROTOCOL;
        ByteBuf buf = Unpooled.copiedBuffer(message, StandardCharsets.UTF_8);
        ClientPlayNetworking.send(CHANNEL, new PacketByteBuf(buf));
    }
}
