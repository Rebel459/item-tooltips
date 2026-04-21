package net.rebel459.item_tooltips;

import net.minecraft.resources.Identifier;
import net.rebel459.item_tooltips.config.ITConfig;
import net.rebel459.unified.platform.UnifiedPlatform;

import java.util.ArrayList;
import java.util.List;

public class ItemTooltips {

	public static boolean enchantmentTooltips = ITConfig.get().enchantments.enchantment_descriptions;
	private static List<String> ENCHANTMENT_TOOLTIP_MODS = new ArrayList<>();

	public static void init() {
		addEnchantmentTooltipMod("idwtialsimmoedm");
		addEnchantmentTooltipMod("enchdesc");
	}

	public static void addEnchantmentTooltipMod(String modId) {
		if (ENCHANTMENT_TOOLTIP_MODS.contains(modId) || !enchantmentTooltips) return;
		ENCHANTMENT_TOOLTIP_MODS.add(modId);
		checkEnchantmentTooltips();
	}

	private static void checkEnchantmentTooltips() {
		if (ITConfig.get().enchantments.auto_disable) {
			for (String modId : ENCHANTMENT_TOOLTIP_MODS) {
				if (UnifiedPlatform.get().isModLoaded(modId)) {
					enchantmentTooltips = false;
				}
			}
		}
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
	public static final String MOD_ID = "item_tooltips";

}