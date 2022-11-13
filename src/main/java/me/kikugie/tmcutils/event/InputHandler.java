package me.kikugie.tmcutils.event;

import fi.dy.masa.malilib.hotkeys.*;
import me.kikugie.tmcutils.TMCUtilsMod;
import me.kikugie.tmcutils.config.Configs;

public class InputHandler implements IKeybindProvider, IKeyboardInputHandler, IMouseInputHandler {
    private static final InputHandler INSTANCE = new InputHandler();

    private InputHandler() {
        super();
    }

    public static InputHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void addKeysToMap(IKeybindManager manager) {
        for (IKeybind keybind : Configs.FEATURE_CONFIGS.getKeybinds()) {
            manager.addKeybindToMap(keybind);
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
        manager.addHotkeysForCategory(TMCUtilsMod.MOD_NAME, "tmc-utils.hotkeys.category", Configs.FEATURE_CONFIGS.getHotkeys());
    }
}
