package me.kikugie.tmcutils.features;

import fi.dy.masa.litematica.data.DataManager;
import me.kikugie.tmcutils.TMCUtilsMod;
import me.kikugie.tmcutils.config.Configs;
import me.kikugie.tmcutils.networking.WorldEditNetworkHandler;
import me.kikugie.tmcutils.util.ResponseMuffler;
import me.kikugie.tmcutils.util.WorldEditStorage;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

public class WorldEditSync {
    private static Box lastBox = null;
    private static int resetCounter = -1;
    private static ClientPlayerEntity player = null;
    private static boolean perfOff = false;

    private static void syncSelection() {
        if (resetCounter > 0) {
            resetCounter--;
        }
        var box = getValidActiveSelection();
        if (box == null) {
            return;
        }
        var mathBox = new Box(box.getPos1(), box.getPos2());
        if (!mathBox.equals(lastBox)) {
            lastBox = mathBox;
            resetCounter = Configs.FeatureConfigs.AUTO_WE_SYNC_TICKS.getIntegerValue();
            return;
        }
        if (resetCounter != 0) {
            return;
        }
        resetCounter = -1;
        if (WorldEditStorage.mode.equals("cuboid")) {
            updateRegion(box);
            TMCUtilsMod.LOGGER.debug("WorldEdit synced!");
            if (Configs.FeatureConfigs.AUTO_WE_SYNC_FEEDBACK.getBooleanValue()) {
                player.sendMessage(Text.of("§oWorldEdit synced!"), true);
            }
        }
    }

    public static void turnOffPerf() {
        if (perfOff) {
            return;
        }
        perfOff = true;
        ResponseMuffler.scheduleMute("Side effect \"Neighbors\".+");
        player.sendChatMessage("//perf neighbors off");
        TMCUtilsMod.LOGGER.debug("Turning off perf");
    }

    public static void onJoinGame() {
        WorldEditNetworkHandler.registerReceiver();
        player = MinecraftClient.getInstance().player;
        perfOff = false;
    }

    public static void onWorldEditConnected() {
        ClientTickEvents.START_WORLD_TICK.register(tick -> {
            if (Configs.FeatureConfigs.AUTO_WE_SYNC.getBooleanValue() && player.hasPermissionLevel(2)) {
                WorldEditSync.syncSelection();
            }
        });
    }

    public static void updateRegion(fi.dy.masa.litematica.selection.Box box) {
        ResponseMuffler.scheduleMute("(\\w+ position set to \\(.+\\).)|(Position already set.)");
        player.sendChatMessage(String.format("//pos1 %d,%d,%d",
                box.getPos1().getX(),
                box.getPos1().getY(),
                box.getPos1().getZ()));
        ResponseMuffler.scheduleMute("(\\w+ position set to \\(.+\\).)|(Position already set.)");
        player.sendChatMessage(String.format("//pos2 %d,%d,%d",
                box.getPos2().getX(),
                box.getPos2().getY(),
                box.getPos2().getZ()));
    }

    @Nullable
    public static fi.dy.masa.litematica.selection.Box getValidActiveSelection() {
        var selection = DataManager.getSelectionManager().getCurrentSelection();
        if (selection == null) {
            return null;
        }
        var box = selection.getSelectedSubRegionBox();
        if (box == null || box.getPos1() == null || box.getPos2() == null) {
            return null;
        }
        return box;
    }
}
