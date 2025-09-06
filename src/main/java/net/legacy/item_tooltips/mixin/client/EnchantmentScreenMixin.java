package net.legacy.item_tooltips.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.legacy.item_tooltips.config.ITConfig;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mixin(EnchantmentScreen.class)
public class EnchantmentScreenMixin {
	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "java/util/List.add(Ljava/lang/Object;)Z", ordinal = 0))
	private boolean enchanting_table_descriptions_addTooltipsToTable(List<Component> instance, Object text, Operation<Boolean> operation, @Local Optional<Holder.Reference<Enchantment>> enchantment) {
		boolean bl = operation.call(instance,text);
		if (!ITConfig.get.enchantments.enchanting_table_descriptions) return bl;
		ResourceLocation enchantmentId = enchantment.get().unwrapKey().get().location();
		MutableComponent description = (Component.literal(""))
				.append(Component.translatable(ITConfig.get.enchantments.prefix).withColor(ITConfig.get.enchantments.prefix_color))
				.append(Component.translatable("enchantment." + enchantmentId.getNamespace() + "." + enchantmentId.getPath() + ".desc").withColor(ITConfig.get.enchantments.color));
		if (!Objects.equals(description,description.getContents())) {
			return instance.add(description);
		} else {
			return bl;
		}
	}
}