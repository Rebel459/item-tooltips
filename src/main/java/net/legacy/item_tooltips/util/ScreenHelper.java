package net.legacy.item_tooltips.util;

import net.legacy.item_tooltips.config.ITConfig;

public class ScreenHelper {
    public static boolean hasShiftDown;
    public static boolean hasAltDown;
    public static boolean hasControlDown;

    public enum Tooltip {
        SHIFT,
        ALT,
        CONTROL;

        public static boolean hasKeyDown() {
            return (ITConfig.get.tooltips.required_key == SHIFT && hasShiftDown) || (ITConfig.get.tooltips.required_key == ALT && hasAltDown) || (ITConfig.get.tooltips.required_key == CONTROL && hasControlDown);
        }

        public static String getString() {
            if (ITConfig.get.tooltips.required_key == SHIFT) return "shift";
            else if (ITConfig.get.tooltips.required_key == ALT) return "alt";
            else if (ITConfig.get.tooltips.required_key == CONTROL) return "control";
            else return null;
        }
    }
}