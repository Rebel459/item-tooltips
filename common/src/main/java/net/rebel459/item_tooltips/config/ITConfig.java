package net.rebel459.item_tooltips.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.rebel459.item_tooltips.ItemTooltips;
import net.rebel459.item_tooltips.util.ScreenHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;


@Config(name = ItemTooltips.MOD_ID)
public class ITConfig implements ConfigData {

    @Contract(pure = true)
    public static @NotNull Path configPath(boolean json5) {
        return Path.of("./config/" + ItemTooltips.MOD_ID + "." + (json5 ? "json5" : "json"));
    }

    public static ITConfig get() {
        return AutoConfig.getConfigHolder(ITConfig.class).getConfig();
    }

    public static void init() {
        AutoConfig.register(ITConfig.class, JanksonConfigSerializer::new);
    }

    public static class Defaults {
        public static final int BLESSING_COLOR = 16755200;
        public static final int ENCHANTMENT_COLOR = 11184810;
        public static final int CURSE_COLOR = 16733525;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public DescriptionConfig descriptions = new DescriptionConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public EnchantmentConfig enchantments = new EnchantmentConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public TooltipConfig tooltips = new TooltipConfig();

    public static class DescriptionConfig {
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean add_descriptions = true;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean require_key_hold = true;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean key_hold_notice = true;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.ColorPicker
        public int color = 5592405;

        @ConfigEntry.Gui.CollapsibleObject
        public PrefixConfig prefix = new PrefixConfig();

        public static class PrefixConfig {
            @ConfigEntry.Category("config")
            @ConfigEntry.Gui.Tooltip
            public String text = "";

            @ConfigEntry.Category("config")
            @ConfigEntry.Gui.Tooltip
            public boolean align_wrapped_text = true;

            @ConfigEntry.Category("config")
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.ColorPicker
            public int color = 13027014;
        }
    }

    public static class EnchantmentConfig {
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean enchantment_descriptions = true;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean enchanting_table_descriptions = true;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean auto_disable = true;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean require_key_hold = true;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean key_hold_notice = false;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.ColorPicker
        public int color = 5592405;

        @ConfigEntry.Gui.CollapsibleObject
        public PrefixConfig prefix = new PrefixConfig();

        public static class PrefixConfig {
            @ConfigEntry.Category("config")
            @ConfigEntry.Gui.Tooltip
            public String text = " ";

            @ConfigEntry.Category("config")
            @ConfigEntry.Gui.Tooltip
            public boolean align_wrapped_text = true;

            @ConfigEntry.Category("config")
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.ColorPicker
            public int color = 13027014;
        }

        @ConfigEntry.Gui.CollapsibleObject
        public NameConfig names = new NameConfig();

        public static class NameConfig {
            @ConfigEntry.Category("config")
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.ColorPicker
            public int blessing_color = Defaults.BLESSING_COLOR;

            @ConfigEntry.Category("config")
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.ColorPicker
            public int enchantment_color = Defaults.ENCHANTMENT_COLOR;

            @ConfigEntry.Category("config")
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.ColorPicker
            public int curse_color = Defaults.CURSE_COLOR;
        }
    }

    public static class TooltipConfig {
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler(option=ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public ScreenHelper.Tooltip required_key = ScreenHelper.Tooltip.SHIFT;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean wrap_text = true;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean retain_empty_space = true;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public int length = -1;

        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min=10, max=100)
        public int length_cap = 50;
    }
}
