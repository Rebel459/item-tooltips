package net.legacy.item_tooltips.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.legacy.item_tooltips.ItemTooltips;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;


@Config(name = ItemTooltips.MOD_ID)
public class ITConfig implements ConfigData {

    @Contract(pure = true)
    public static @NotNull Path configPath(boolean json5) {
        return Path.of("./config/" + ItemTooltips.MOD_ID + "." + (json5 ? "json5" : "json"));
    }

    public static ITConfig get;

    public static void init() {
        AutoConfig.register(ITConfig.class, JanksonConfigSerializer::new);
        get = AutoConfig.getConfigHolder(ITConfig.class).getConfig();
    }

    @ConfigEntry.Gui.CollapsibleObject
    public DescriptionConfig descriptions = new DescriptionConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public TooltipConfig tooltips = new TooltipConfig();

    public static class DescriptionConfig {
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean add_descriptions = true;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean require_shift = true;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean shift_notice = true;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public int color = 5592405;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public String prefix = "";

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public int prefix_color = 13027014;
    }

    public static class TooltipConfig {
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean wrap_text = true;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public int length = -1;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public int length_cap = 50;
    }
}
