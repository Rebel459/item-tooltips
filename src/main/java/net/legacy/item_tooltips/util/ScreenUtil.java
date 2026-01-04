package net.legacy.item_tooltips.util;

@Deprecated
public class ScreenUtil {
    public static boolean hasShiftDown;
    public static boolean hasAltDown;
    public static boolean hasControlDown;

    public static boolean hasTooltipKeyDown() {
        return ScreenHelper.Tooltip.hasKeyDown();
    }

    public enum TooltipKey {
        SHIFT,
        ALT,
        CONTROL;

        public static String getString() {
            return ScreenHelper.Tooltip.getString();
        }
    }
}