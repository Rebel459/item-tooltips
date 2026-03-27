package net.rebel459.item_tooltips;

import net.minecraft.resources.Identifier;
import net.rebel459.item_tooltips.config.ITConfig;
import net.rebel459.unified.platform.UnifiedHelpers;

public class ItemTooltips {

	public static boolean enchantmentTooltips;

	public static void init() {

        enchantmentTooltips = ITConfig.get.enchantments.enchantment_descriptions;
		if (ITConfig.get.enchantments.auto_disable) {
			checkEnchantmentTooltips("idwtialsimmoedm");
			checkEnchantmentTooltips("enchdesc");
		};
	}

	public static void checkEnchantmentTooltips(String modId) {
		if (UnifiedHelpers.PLATFORM.isModLoaded(modId)) {
			enchantmentTooltips = false;
		}
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
	public static final String MOD_ID = "item_tooltips";

}