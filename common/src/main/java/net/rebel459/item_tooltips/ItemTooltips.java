package net.rebel459.item_tooltips;

import net.minecraft.resources.Identifier;
import net.rebel459.item_tooltips.config.ITConfig;
import net.rebel459.unified.platform.UnifiedPlatform;

import java.util.ArrayList;
import java.util.List;

public class ItemTooltips {

	public static boolean enchantmentTooltips = ITConfig.get.enchantments.enchantment_descriptions;
	public static List<String> ENCHANTMENT_TOOLTIP_MODS = new ArrayList<>();

	public static void init() {

		ENCHANTMENT_TOOLTIP_MODS.add("idwtialsimmoedm");
		ENCHANTMENT_TOOLTIP_MODS.add("enchdesc");
		if (ITConfig.get.enchantments.auto_disable) {
			for (String modName : ENCHANTMENT_TOOLTIP_MODS) {
				checkEnchantmentTooltips(modName);
			}
		}
	}

	public static void checkEnchantmentTooltips(String modId) {
		if (UnifiedPlatform.get().isModLoaded(modId)) {
			enchantmentTooltips = false;
		}
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
	public static final String MOD_ID = "item_tooltips";

}