package net.rebel459.item_tooltips.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.rebel459.item_tooltips.ItemTooltips;
import org.jetbrains.annotations.NotNull;

public class ITEnchantmentTags {
    public static final TagKey<Enchantment> BLESSING = create("blessing");

    @NotNull
    private static TagKey<Enchantment> create(@NotNull String path) {
        return TagKey.create(Registries.ENCHANTMENT, ItemTooltips.id(path));
    }
}
