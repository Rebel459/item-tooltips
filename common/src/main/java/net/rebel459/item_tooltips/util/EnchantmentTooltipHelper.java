package net.rebel459.item_tooltips.util;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.*;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.rebel459.item_tooltips.config.ITConfig;
import net.rebel459.item_tooltips.tag.ITEnchantmentTags;

public final class EnchantmentTooltipHelper {

    public static MutableComponent createName(Holder<Enchantment> enchantment, int level) {
        MutableComponent mutableComponent = enchantment.value().description().copy();
        if (enchantment.value().getMaxLevel() > 1) mutableComponent.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + level));

        int color = EnchantmentTooltipHelper.getColor(enchantment);
        mutableComponent = ComponentUtils.mergeStyles(mutableComponent, Style.EMPTY.withColor(color));

        return getComponent(mutableComponent);
    }

    public static MutableComponent getComponent(MutableComponent enchantName) {
        return enchantName;
    }

    public static int getColor(Holder<Enchantment> enchantment) {
        ITConfig.EnchantmentConfig.NameConfig enchantmentNames = ITConfig.get().enchantments.names;
        if (enchantment.is(ITEnchantmentTags.BLESSING)) {
            return enchantmentNames.blessing_color;
        }
        else if (enchantment.is(EnchantmentTags.CURSE)) {
            return enchantmentNames.curse_color;
        }
        else {
            return enchantmentNames.enchantment_color;
        }
    }
}
