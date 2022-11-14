package me.kikugie.tmcutils.config;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

public enum DyeColorOption implements IConfigOptionListEntry {
    DEFAULT("default", "§oDefault"),
    BLACK("black", "§0Black"),
    GRAY("gray", "§8Gray"),
    LIGHT_GRAY("light_gray", "§7Light Gray"),
    WHITE("white", "§fWhite"),
    RED("red", "§4Red"),
    ORANGE("orange", "§6Orange"),
    YELLOW("yellow", "§eYellow"),
    LIME("lime", "§aLime"),
    GREEN("green", "§2Green"),
    CYAN("cyan", "§3Cyan"),
    LIGHT_BLUE("light_blue", "§bLight Blue"),
    BLUE("blue", "§1Blue"),
    PURPLE("purple", "§5Purple"),
    MAGENTA("magenta", "§dMagenta"),
    PINK("pink", "§cPink"),
    BROWN("brown", "§6Brown");

    private final String color;
    private final String displayName;

    DyeColorOption(String color, String displayName) {
        this.color = color;
        this.displayName = displayName;
    }

    @Override
    public String getStringValue() {
        return this.color;
    }

    @Override
    public String getDisplayName() {
        return StringUtils.translate(this.displayName);
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward) {
        int mod = forward ? 1 : -1;
        return values()[(this.ordinal() + mod) % values().length];
    }

    @Override
    public IConfigOptionListEntry fromString(String value) {
        DyeColorOption[] vals = values();

        for (DyeColorOption temp : vals) {
            if (temp.color.equalsIgnoreCase(value)) {
                return temp;
            }
        }

        return DEFAULT;
    }
}
