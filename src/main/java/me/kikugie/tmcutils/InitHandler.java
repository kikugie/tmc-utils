package me.kikugie.tmcutils;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import me.kikugie.tmcutils.config.ConfigHandler;
import me.kikugie.tmcutils.event.InputHandler;
import me.kikugie.tmcutils.event.KeyCallbacks;

public class InitHandler implements IInitializationHandler {
    @Override
    public void registerModHandlers() {
        ConfigManager.getInstance().registerConfigHandler(TMCUtilsMod.MOD_ID, new ConfigHandler());
        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        KeyCallbacks.init();
    }
}
