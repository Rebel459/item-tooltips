package net.legacy.item_tooltips.mixin.item;

import net.legacy.item_tooltips.ItemTooltips;
import net.legacy.item_tooltips.config.ITConfig;
import net.legacy.item_tooltips.registry.ITItemTags;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "appendHoverText", at = @At(value = "HEAD"))
    private void addShiftDescription(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (stack.is(ITItemTags.HAS_DESCRIPTION)) {
            MutableComponent prefixText = Component.translatable(ITConfig.get.tooltip_prefix).withColor(ITConfig.get.tooltip_prefix_color);
            MutableComponent descriptionText = Component.translatable(stack.getItem().getDescriptionId() + ".desc").withColor(ITConfig.get.tooltip_color);
            if (ITConfig.get.require_shift) {
                if (Screen.hasShiftDown()) {
                    tooltipComponents.add(Component.translatable("").append(prefixText).append(descriptionText));
                }
                else if (ITConfig.get.tooltip_notice)
                    tooltipComponents.add(Component.translatable("tooltip." + ItemTooltips.MOD_ID + ".hold_shift").withColor(ITConfig.get.tooltip_color));
            }
            else tooltipComponents.add(Component.translatable("").append(prefixText).append(descriptionText));
        }
    }
}
