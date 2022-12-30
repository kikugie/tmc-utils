package me.kikugie.tmcutils.gui;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import me.kikugie.tmcutils.TMCUtilsMod;
import me.kikugie.tmcutils.config.Configs;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ConfigGui extends GuiConfigsBase {
    private static GuiTabs tab = GuiTabs.FEATURES;

    public ConfigGui() {
        super(10, 50, TMCUtilsMod.MOD_ID, null, "tmc-utils.gui.config.title", TMCUtilsMod.MOD_VERSION);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();

        int x = 10;
        int y = 26;
        int rows = 1;

        for (GuiTabs tab : GuiTabs.values()) {
            int width = this.getStringWidth(tab.getDisplayName()) + 10;

            if (x >= this.width - width - 10) {
                x = 10;
                y += 22;
                rows++;
            }

            x += this.createButton(x, y, width, tab);
        }

        if (rows > 1) {
            int scrollbarPosition = this.getListWidget().getScrollbar().getValue();
            this.setListPosition(this.getListX(), 50 + (rows - 1) * 22);
            this.reCreateListWidget();
            this.getListWidget().getScrollbar().setValue(scrollbarPosition);
            this.getListWidget().refreshEntries();
        }
    }

    private int createButton(int x, int y, int width, GuiTabs tab) {
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getDisplayName());
        button.setEnabled(ConfigGui.tab != tab);
        this.addButton(button, new ButtonListener(tab, this));

        return button.getWidth() + 2;
    }

    @Override
    protected int getConfigWidth() {
        GuiTabs tab = ConfigGui.tab;

//        // There are no keybinds here so this doesn't need to be huge
//        if (tab == GuiTabs.FEATURES || tab == GuiTabs.TWEAKS || tab == GuiTabs.RENDER || tab == GuiTabs.DEBUG_RENDERER || tab == GuiTabs.BUG_FIX) {
//            return 100;
//        }

        return super.getConfigWidth();
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        List<? extends IConfigBase> configs;
        GuiTabs tab = ConfigGui.tab;

        if (tab == GuiTabs.FEATURES) {
            configs = Configs.MISC_CONFIGS.get();
        } else {
            return Collections.emptyList();
        }

        return ConfigOptionWrapper.createFor(configs);
    }

    public enum GuiTabs {
        FEATURES("tmc-utils.gui.config.features");
        private final String translationKey;

        GuiTabs(String translationKey) {
            this.translationKey = translationKey;
        }

        public String getDisplayName() {
            return StringUtils.translate(this.translationKey);
        }

    }

    private record ButtonListener(GuiTabs tab, ConfigGui parent) implements IButtonActionListener {
        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {

            ConfigGui.tab = this.tab;

            this.parent.reCreateListWidget(); // apply the new config width
            Objects.requireNonNull(this.parent.getListWidget()).resetScrollbarPosition();
            this.parent.initGui();
        }
    }
}
