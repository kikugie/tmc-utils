package me.kikugie.tmcutils.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybind;

import java.util.ArrayList;
import java.util.List;

public class Configs {
    public static MiscConfigs MISC_CONFIGS = new MiscConfigs(ImmutableList.of(
            MiscConfigs.ISORENDER_SELECTION,
            MiscConfigs.GIVE_FULL_BOX,
            MiscConfigs.BOX_COLOR
    ));

    public static WorldEditConfigs WORLD_EDIT_CONFIGS = new WorldEditConfigs(ImmutableList.of(
            WorldEditConfigs.AUTO_WE_SYNC,
            WorldEditConfigs.AUTO_WE_SYNC_TICKS,
            WorldEditConfigs.AUTO_WE_SYNC_FEEDBACK,
            WorldEditConfigs.AUTO_PERF_OFF
    ));

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

    public static class WorldEditConfigs extends BaseConfigs {

        public static final ConfigBooleanHotkeyed AUTO_WE_SYNC = new ConfigBooleanHotkeyed("autoWeSync", false, "", "Synchronises WorldEdit selection n ticks after configured value");
        public static final ConfigInteger AUTO_WE_SYNC_TICKS = new ConfigInteger("autoWeSyncTicks", 20, 1, 1000, false, "Ticks to wait before synchronising WorldEdit selection");
        public static final ConfigBoolean AUTO_WE_SYNC_FEEDBACK = new ConfigBoolean("autoWeSyncFeedback", true, "Send feedback to player when WorldEdit selection is synced");
        public static final ConfigBoolean AUTO_PERF_OFF = new ConfigBoolean("autoPerfOff", true, "Automatically disables WorldEdit neighbour updates on log in");

        public WorldEditConfigs(ImmutableList<IConfigBase> options) {
            super(options);
        }
    }
    public static class MiscConfigs extends BaseConfigs {
        public static final ConfigHotkey ISORENDER_SELECTION = new ConfigHotkey("isorenderSelection", "I", "Render current Litematica selection");
        public static final ConfigHotkey GIVE_FULL_BOX = new ConfigHotkey("giveFullBox", "G", "Gives a full box of the item in your hand");
        public static final ConfigOptionList BOX_COLOR = new ConfigOptionList("boxColor", DyeColorOption.WHITE, "Color of the box");

        public MiscConfigs(ImmutableList<IConfigBase> options) {
            super(options);
        }
    }


}
