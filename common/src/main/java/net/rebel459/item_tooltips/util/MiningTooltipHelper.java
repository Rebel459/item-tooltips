package net.rebel459.item_tooltips.util;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class MiningTooltipHelper {

    private static final ThreadLocal<ItemStack> CURRENT_STACK = new ThreadLocal<>();

    private MiningTooltipHelper() {}

    public static void setStack(ItemStack stack) {
        CURRENT_STACK.set(stack);
    }

    @Nullable
    public static ItemStack getStack() {
        return CURRENT_STACK.get();
    }

    public static void clear() {
        CURRENT_STACK.remove();
    }
}
