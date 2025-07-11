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

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    public boolean require_shift = true;

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    public boolean tooltip_notice = false;

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    public int tooltip_color = 5592405;

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    public String tooltip_prefix = "";

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    public int tooltip_prefix_color = 13027014;

}
