package me.kikugie.tmcutils.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import me.kikugie.tmcutils.TMCUtilsMod;

import java.util.ArrayList;
import java.util.List;

public class Configs {
    public static FeatureConfigs FEATURE_CONFIGS = new FeatureConfigs();

    public static class BaseConfigs {
        public final ImmutableList<IConfigBase> OPTIONS;

        public BaseConfigs(ImmutableList<IConfigBase> options) {
            this.OPTIONS = options;
        }

        public ImmutableList<IConfigBase> get() {
            return OPTIONS;
        }

        public ImmutableList<IHotkey> getHotkeys() {
            List<IHotkey> list = new ArrayList<>();
            for (IConfigBase configValue : this.OPTIONS) {
                if (configValue instanceof IHotkey) {
                    list.add(((IHotkey) configValue));
                }
            }
            return ImmutableList.copyOf(list);
        }

        public ImmutableList<IKeybind> getKeybinds() {
            List<IKeybind> list = new ArrayList<>();
            for (IConfigBase configValue : this.OPTIONS) {
                if (configValue instanceof IHotkey) {
                    list.add(((IHotkey) configValue).getKeybind());
                }
            }
            return ImmutableList.copyOf(list);
        }
    }

    public static class FeatureConfigs extends BaseConfigs {
        public static final ConfigHotkey ISORENDER_SELECTION = new ConfigHotkey("isorenderSelection", "I", "Render current Litematica selection");
        public static final ConfigBooleanHotkeyed AUTO_WE_SYNC = new ConfigBooleanHotkeyed("autoWeSync", false, "", "Synchronises WorldEdit selection n ticks after configured value");
        public static final ConfigInteger AUTO_WE_SYNC_TICKS = new ConfigInteger("autoWeSyncTicks", 20, 1, 1000, false, "Ticks to wait before synchronising WorldEdit selection");
        public static final ConfigBoolean AUTO_WE_SYNC_FEEDBACK = new ConfigBoolean("autoWeSyncFeedback", true, "Send feedback to player when WorldEdit selection is synced");
        public static final ConfigBoolean AUTO_PERF_OFF = new ConfigBoolean("autoPerfOff", true, "Automatically disables WorldEdit neighbour updates on log in");
        public static final ConfigHotkey GIVE_FULL_BOX = new ConfigHotkey("giveFullBox", "G", "Gives a full box of the item in your hand");
        public static final ConfigOptionList BOX_COLOR = new ConfigOptionList("boxColor", DyeColorOption.WHITE, "Color of the box");

        public FeatureConfigs() {
            super(getOptions());
        }

        private static ImmutableList<IConfigBase> getOptions() {
            List<IConfigBase> listInProcess = new ArrayList<>(List.of(AUTO_WE_SYNC, AUTO_WE_SYNC_TICKS, AUTO_PERF_OFF, GIVE_FULL_BOX, BOX_COLOR));

            // TODO: Disable options instead of removing them
            if (TMCUtilsMod.isIsoRenderInstalled()) {
                listInProcess.add(ISORENDER_SELECTION);
            }

            return ImmutableList.copyOf(listInProcess);
        }
    }


}
