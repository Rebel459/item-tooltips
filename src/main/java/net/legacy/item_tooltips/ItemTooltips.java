package net.legacy.item_tooltips;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.legacy.item_tooltips.config.ITConfig;
import net.minecraft.resources.Identifier;

public class ItemTooltips implements ClientModInitializer {

	public static boolean enchantmentTooltips;

	@Override
	public void onInitializeClient() {

		ITConfig.init();

        enchantmentTooltips = ITConfig.get.enchantments.enchantment_descriptions;
		if (ITConfig.get.enchantments.auto_disable) {
			checkEnchantmentTooltips("idwtialsimmoedm");
			checkEnchantmentTooltips("enchdesc");
		};
	}

	public static void checkEnchantmentTooltips(String modId) {
		if (FabricLoader.getInstance().isModLoaded(modId)) {
			enchantmentTooltips = false;
		}
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
	public static final String MOD_ID = "item_tooltips";

}