package net.legacy.item_tooltips.mixin.item;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.legacy.item_tooltips.ItemTooltips;
import net.legacy.item_tooltips.config.ITConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    private void addEnchantmentDescription(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        if (!ItemTooltips.enchantmentTooltips || (ITConfig.get.enchantments.require_shift && !Screen.hasShiftDown())) {
            return;
        }

        ItemStack stack = ItemStack.class.cast(this);
        List<Component> tooltip = cir.getReturnValue();

        HashSet<Object2IntMap.Entry<Holder<Enchantment>>> enchantments = new HashSet<>(stack.getEnchantments().entrySet());
        enchantments.addAll(stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).entrySet());
        if (enchantments.isEmpty()) return;

        for (Object2IntMap.Entry<Holder<Enchantment>> enchantmentEntry : enchantments) {
            Enchantment enchantment = enchantmentEntry.getKey().value();

            for (int x = 0; x < tooltip.size(); x++) {
                if (!tooltip.get(x).getContents().equals(enchantment.description().getContents())) continue;

                ResourceLocation enchantmentId = enchantmentEntry.getKey().unwrapKey().get().location();
                MutableComponent description = (Component.literal("")
                        .append(Component.translatable(ITConfig.get.enchantments.prefix).withColor(ITConfig.get.enchantments.prefix_color))
                        .append(Component.translatable("enchantment." + enchantmentId.getNamespace() + "." + enchantmentId.getPath() + ".desc").withColor(ITConfig.get.enchantments.color)));

                tooltip.add(x + 1, description);
            }
        }
    }
}