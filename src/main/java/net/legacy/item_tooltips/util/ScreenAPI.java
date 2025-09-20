package net.legacy.item_tooltips.util;

import net.legacy.item_tooltips.config.ITConfig;

public class ScreenAPI {
    public static boolean hasShiftDown;
    public static boolean hasAltDown;
    public static boolean hasControlDown;

    public static boolean hasTooltipKeyDown() {
        return (ITConfig.get.tooltips.required_key == TooltipKey.SHIFT && hasShiftDown) || (ITConfig.get.tooltips.required_key == TooltipKey.ALT && hasAltDown) || (ITConfig.get.tooltips.required_key == TooltipKey.CONTROL && hasControlDown);
    }

    public enum TooltipKey {
        SHIFT,
        ALT,
        CONTROL;

        public static String getString() {
            if (ITConfig.get.tooltips.required_key == SHIFT) return "shift";
            else if (ITConfig.get.tooltips.required_key == ALT) return "alt";
            else if (ITConfig.get.tooltips.required_key == CONTROL) return "control";
            else return null;
        }
    }
}