package net.rebel459.item_tooltips.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.rebel459.item_tooltips.ItemTooltips;
import org.jetbrains.annotations.NotNull;

public class ITItemTags {

    public static final TagKey<Item> HAS_DESCRIPTION = create("has_description");
    public static final TagKey<Item> NO_DESCRIPTION_NOTICE = create("util/no_description_notice");
    public static final TagKey<Item> DESCRIPTION_BLACKLIST = create("util/description_blacklist");

    @NotNull
    private static TagKey<Item> create(@NotNull String path) {
        return TagKey.create(Registries.ITEM, ItemTooltips.id(path));
    }
}