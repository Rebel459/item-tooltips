package net.legacy.item_tooltips.tag;

import net.legacy.item_tooltips.ItemTooltips;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class ITEnchantmentTags {
    public static final TagKey<Enchantment> BLESSING = register("blessing");

    @NotNull
    private static TagKey<Enchantment> register(@NotNull String path) {
        return TagKey.create(Registries.ENCHANTMENT, ItemTooltips.id(path));
    }
}
