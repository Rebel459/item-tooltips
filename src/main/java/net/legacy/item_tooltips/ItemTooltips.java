package net.legacy.item_tooltips;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.legacy.item_tooltips.config.ITConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.HashSet;
import java.util.Map;

public class ItemTooltips implements ModInitializer {

	public static boolean enchantmentTooltips;

	@Override
	public void onInitialize() {

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

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
	public static final String MOD_ID = "item_tooltips";

}