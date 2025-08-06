package net.legacy.item_tooltips;

import net.legacy.item_tooltips.config.ITConfig;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(ItemTooltips.MOD_ID)
public class ItemTooltips {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "item_tooltips";
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ItemTooltips.MOD_ID, path);
    }
    public ItemTooltips(ModContainer modContainer) {
        ITConfig.init();
    }
}
