package net.legacy.item_tooltips.mixin.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.legacy.item_tooltips.ItemTooltips;
import net.legacy.item_tooltips.config.ITConfig;
import net.legacy.item_tooltips.registry.ITItemTags;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract boolean is(TagKey<Item> tagKey);

    @Inject(method = "addDetailsToTooltip", at = @At(value = "HEAD"))
    private void addDescription(Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Player player, TooltipFlag tooltipFlag, Consumer<Component> consumer, CallbackInfo ci) {
        if (!ITConfig.get.descriptions.add_descriptions || this.is(ITItemTags.DESCRIPTION_BLACKLIST)) return;
        if (this.is(ITItemTags.HAS_DESCRIPTION)) {
            MutableComponent prefixText = Component.translatable(ITConfig.get.descriptions.prefix).withColor(ITConfig.get.descriptions.prefix_color);
            MutableComponent descriptionText = Component.translatable(this.getItem().getDescriptionId() + ".desc").withColor(ITConfig.get.descriptions.color);
            if ((ITConfig.get.descriptions.require_shift)) {
                if (Screen.hasShiftDown())
                    consumer.accept(Component.translatable("").append(prefixText).append(descriptionText));
                else if (ITConfig.get.descriptions.shift_notice && !this.is(ITItemTags.NO_SHIFT_NOTICE))
                    consumer.accept(Component.translatable("tooltip." + ItemTooltips.MOD_ID + ".hold_shift").withColor(ITConfig.get.descriptions.color));
                else consumer.accept(Component.translatable("").append(prefixText).append(descriptionText));
            }
        }
    }
}