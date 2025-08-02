package net.legacy.item_tooltips.mixin.item;

import net.legacy.item_tooltips.ItemTooltips;
import net.legacy.item_tooltips.config.ITConfig;
import net.legacy.item_tooltips.registry.ITItemTags;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow public abstract ItemStack getDefaultInstance();

    @Inject(method = "appendHoverText", at = @At(value = "HEAD"))
    private void addDescription(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (!ITConfig.get.descriptions.add_descriptions || this.getDefaultInstance().is(ITItemTags.DESCRIPTION_BLACKLIST)) return;
        if (this.getDefaultInstance().is(ITItemTags.HAS_DESCRIPTION)) {
            MutableComponent prefixText = Component.translatable(ITConfig.get.descriptions.prefix).withColor(ITConfig.get.descriptions.prefix_color);
            MutableComponent descriptionText = Component.translatable(this.getDefaultInstance().getDescriptionId() + ".desc").withColor(ITConfig.get.descriptions.color);
            if (ITConfig.get.descriptions.require_shift) {
                if (Screen.hasShiftDown())
                    list.add(Component.translatable("").append(prefixText).append(descriptionText));
                else if (ITConfig.get.descriptions.shift_notice && !this.getDefaultInstance().is(ITItemTags.NO_SHIFT_NOTICE))
                    list.add(Component.translatable("tooltip." + ItemTooltips.MOD_ID + ".hold_shift").withColor(ITConfig.get.descriptions.color));
                else list.add(Component.translatable("").append(prefixText).append(descriptionText));
            }
        }
    }
}
