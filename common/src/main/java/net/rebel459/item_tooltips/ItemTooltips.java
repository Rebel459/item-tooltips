package net.rebel459.item_tooltips;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.rebel459.item_tooltips.config.ITConfig;
import net.rebel459.item_tooltips.tag.ITItemTags;
import net.rebel459.unified.platform.UnifiedPlatform;
import net.rebel459.unified.platform.client.UnifiedClientEvents;

import java.util.ArrayList;
import java.util.List;

public class ItemTooltips {

	public static boolean enchantmentTooltips = ITConfig.get().enchantments.enchantment_descriptions;
	static List<String> ENCHANTMENT_TOOLTIP_MODS = new ArrayList<>();

	public static void init() {
		addEnchantmentTooltipMod("idwtialsimmoedm");
		addEnchantmentTooltipMod("enchdesc");

		UnifiedClientEvents.ItemTooltips.afterBaseAttributeAdded((builder, stack, itemModifiers, player, attribute, displayValue) -> {
			if (!ITConfig.get().items.mining_speed_tooltip) return;
			if (!attribute.is(Attributes.ATTACK_SPEED) || stack == null || !stack.has(DataComponents.TOOL) || stack.is(ITItemTags.HIDDEN_MINING_SPEED)) return;

			Tool tool = stack.get(DataComponents.TOOL);
			float miningSpeed = tool.rules().stream()
					.filter(rule -> rule.correctForDrops().orElse(false))
					.flatMap(rule -> rule.speed().stream())
					.max(Float::compare)
					.orElse(tool.defaultMiningSpeed());

			if (miningSpeed == 1F) return;

			builder.accept(
					Component.literal(" ")
							.append(Component.literal(ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(miningSpeed) + " "))
							.append(Component.translatable("tooltip.item_tooltips.mining_speed"))
							.withStyle(ChatFormatting.DARK_GREEN)
			);
		});
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