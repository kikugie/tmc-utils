package me.kikugie.tmcutils.event;

import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import me.kikugie.tmcutils.config.Configs;
import me.kikugie.tmcutils.features.GiveFullBox;
import net.fabricmc.fabric.impl.command.client.ClientCommandInternals;

public class KeyCallbacks {
    public static void init() {
        Configs.FeatureConfigs.ISORENDER_SELECTION.getKeybind().setCallback(new IsorenderSelectionCallback());
        Configs.FeatureConfigs.GIVE_FULL_BOX.getKeybind().setCallback(new GiveFullBoxCallback());
    }

    private static class IsorenderSelectionCallback implements IHotkeyCallback {
        @Override
        public boolean onKeyAction(KeyAction action, IKeybind key) {
            if (key == Configs.FeatureConfigs.ISORENDER_SELECTION.getKeybind()) {
                // TODO: Call something directly instead of using the command :P
                ClientCommandInternals.executeCommand("isorender selection");
                return true;
            }
            return false;
        }
    }

    private static class GiveFullBoxCallback implements IHotkeyCallback {
        @Override
        public boolean onKeyAction(KeyAction action, IKeybind key) {
            if (key == Configs.FeatureConfigs.GIVE_FULL_BOX.getKeybind()) {
                GiveFullBox.giveBox();
            }
            return false;
        }
    }
}
