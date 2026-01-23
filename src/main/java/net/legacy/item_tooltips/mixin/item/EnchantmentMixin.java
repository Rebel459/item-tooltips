package net.legacy.item_tooltips.mixin.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.legacy.item_tooltips.config.ITConfig;
import net.legacy.item_tooltips.tag.ITEnchantmentTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(value = Enchantment.class, priority = 1)
public abstract class EnchantmentMixin {

    @Inject(method = "getFullname", at = @At("TAIL"), cancellable = true)
    private static void recolourEnchantments(Holder<Enchantment> enchantment, int i, CallbackInfoReturnable<Component> cir) {
        MutableComponent mutableComponent = enchantment.value().description().copy();
        if (enchantment.is(ITEnchantmentTags.BLESSING) && (ITConfig.get.enchantments.names.blessing_color != ITConfig.Defaults.BLESSING_COLOR) || !FabricLoader.getInstance().isModLoaded("enchants_and_expeditions")) {
            mutableComponent = ComponentUtils.mergeStyles(mutableComponent, Style.EMPTY.withColor(ITConfig.get.enchantments.names.blessing_color));
            cir.setReturnValue(mutableComponent);
        }
        else if (enchantment.is(EnchantmentTags.CURSE) && ITConfig.get.enchantments.names.curse_color != ITConfig.Defaults.CURSE_COLOR) {
            mutableComponent = ComponentUtils.mergeStyles(mutableComponent, Style.EMPTY.withColor(ITConfig.get.enchantments.names.curse_color));
            cir.setReturnValue(mutableComponent);
        }
        else if (ITConfig.get.enchantments.names.enchantment_color != ITConfig.Defaults.ENCHANTMENT_COLOR) {
            mutableComponent = ComponentUtils.mergeStyles(mutableComponent, Style.EMPTY.withColor(ITConfig.get.enchantments.names.enchantment_color));
            cir.setReturnValue(mutableComponent);
        }
    }
}