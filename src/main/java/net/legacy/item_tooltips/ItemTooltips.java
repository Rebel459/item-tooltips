package net.legacy.item_tooltips;

import net.fabricmc.api.ModInitializer;
import net.legacy.item_tooltips.config.ITConfig;
import net.minecraft.resources.ResourceLocation;

public class ItemTooltips implements ModInitializer {
	@Override
	public void onInitialize() {

		ITConfig.init();

	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
	public static final String MOD_ID = "item_tooltips";

}