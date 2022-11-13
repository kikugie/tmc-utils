package me.kikugie.tmcutils.features;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.selection.AreaSelection;
import me.kikugie.tmcutils.TMCUtilsMod;
import me.kikugie.tmcutils.config.Configs;
import me.kikugie.tmcutils.networking.WorldEditNetworkHandler;
import me.kikugie.tmcutils.util.ResponseMuffler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.math.Box;

public class WorldEditSync {
    private static Box lastBox = null;
    private static int resetCounter = -1;

    public static void updateRegion(fi.dy.masa.litematica.selection.Box box) {
        ResponseMuffler.scheduleMute("(\\w+ position set to \\(.+\\).)|(Position already set.)");
        MinecraftClient.getInstance().player.sendCommand(String.format("/pos1 %d,%d,%d",
                box.getPos1().getX(),
                box.getPos1().getY(),
                box.getPos1().getZ()));
        ResponseMuffler.scheduleMute("(\\w+ position set to \\(.+\\).)|(Position already set.)");
        MinecraftClient.getInstance().player.sendCommand(String.format("/pos2 %d,%d,%d",
                box.getPos2().getX(),
                box.getPos2().getY(),
                box.getPos2().getZ()));
    }

    public static boolean isLitematicaSelectionValid(AreaSelection selection) {
        if (selection == null) {
            return false;
        }
        var box = selection.getSelectedSubRegionBox();
        if (box == null) {
            return false;
        }
        return box.getPos1() != null && box.getPos2() != null;
    }

    public static void onJoinGame(ClientPlayNetworkHandler ignoredHandler, PacketSender ignoredSender, MinecraftClient ignoredClient) {
        ClientTickEvents.START_WORLD_TICK.register(tick -> {
            if (Configs.FeatureConfigs.AUTO_WE_SYNC.getBooleanValue() && WorldEditNetworkHandler.isWorldEditConnected() && MinecraftClient.getInstance().player.hasPermissionLevel(2)) {
                WorldEditSync.syncSelection();
            }
        });
        WorldEditNetworkHandler.registerReceiver();
    }

    public static void syncSelection() {
        // TODO: Don't sync if selection is not cuboid
        if (resetCounter > 0) {
            resetCounter--;
        }
        var selection = DataManager.getSelectionManager().getCurrentSelection();
        if (!isLitematicaSelectionValid(selection)) {
            return;
        }
        var box = selection.getSelectedSubRegionBox();
        var mathBox = new Box(box.getPos1(), box.getPos2());

        if (!mathBox.equals(lastBox)) {
            lastBox = mathBox;
            resetCounter = Configs.FeatureConfigs.AUTO_WE_SYNC_TICKS.getIntegerValue();
            return;
        }
        if (resetCounter == 0) {
            updateRegion(box);
            resetCounter = -1;
            TMCUtilsMod.LOGGER.debug("WorldEdit synced!");
        }
    }
}
