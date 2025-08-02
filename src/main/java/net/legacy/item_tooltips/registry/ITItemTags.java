package net.legacy.item_tooltips.registry;

import net.legacy.item_tooltips.ItemTooltips;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class ITItemTags {

    public static final TagKey<Item> HAS_DESCRIPTION = bind("has_description");
    public static final TagKey<Item> NO_SHIFT_NOTICE = bind("util/no_shift_notice");
    public static final TagKey<Item> DESCRIPTION_BLACKLIST = bind("util/description_blacklist");

    @NotNull
    private static TagKey<Item> bind(@NotNull String path) {
        return TagKey.create(Registries.ITEM, ItemTooltips.id(path));
    }

}