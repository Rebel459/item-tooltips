package net.rebel459.item_tooltips.util;

import net.minecraft.client.Minecraft;
import net.rebel459.item_tooltips.config.ITConfig;

public class ScreenHelper {
    @Deprecated
    public static boolean hasShiftDown;
    @Deprecated
    public static boolean hasAltDown;
    @Deprecated
    public static boolean hasControlDown;

    public static boolean hasKeyDown() {
        Minecraft client = Minecraft.getInstance();
        return (ITConfig.get().tooltips.required_key == Tooltip.SHIFT && client.hasShiftDown()) || (ITConfig.get().tooltips.required_key == Tooltip.ALT && client.hasAltDown()) || (ITConfig.get().tooltips.required_key == Tooltip.CONTROL && client.hasControlDown());
    }

    public enum Tooltip {
        SHIFT,
        ALT,
        CONTROL;

        @Deprecated
        public static boolean hasKeyDown() {
            return ScreenHelper.hasKeyDown();
        }

        public static String getString() {
            if (ITConfig.get().tooltips.required_key == SHIFT) return "shift";
            else if (ITConfig.get().tooltips.required_key == ALT) return "alt";
            else if (ITConfig.get().tooltips.required_key == CONTROL) return "control";
            else return null;
        }
    }
}