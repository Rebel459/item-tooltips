package net.legacy.item_tooltips.util;

import net.legacy.item_tooltips.ItemTooltips;
import net.legacy.item_tooltips.config.ITConfig;
import net.minecraft.network.chat.Component;

public class ScreenAPI {
    public static boolean hasShiftDown;
    public static boolean hasAltDown;
    public static boolean hasControlDown;

    public static boolean hasTooltipKeyDown() {
        return (ITConfig.get.tooltips.required_key == ITConfig.KeyType.SHIFT && hasShiftDown) || (ITConfig.get.tooltips.required_key == ITConfig.KeyType.ALT && hasAltDown) || (ITConfig.get.tooltips.required_key == ITConfig.KeyType.CONTROL && hasControlDown);
    }

    public static String requiredKeyString() {
        if (ITConfig.get.tooltips.required_key == ITConfig.KeyType.SHIFT) return "shift";
        else if (ITConfig.get.tooltips.required_key == ITConfig.KeyType.ALT) return "alt";
        else if (ITConfig.get.tooltips.required_key == ITConfig.KeyType.CONTROL) return "control";
        else return null;
    }
}