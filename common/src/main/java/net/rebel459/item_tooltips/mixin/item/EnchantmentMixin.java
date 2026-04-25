package net.rebel459.item_tooltips.mixin.item;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.rebel459.item_tooltips.util.EnchantmentTooltipHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Enchantment.class)
public abstract class EnchantmentMixin {

    @Inject(method = "getFullname", at = @At("HEAD"), cancellable = true)
    private static void IT$getFullName(Holder<Enchantment> enchantment, int level, CallbackInfoReturnable<Component> cir) {
        cir.setReturnValue(EnchantmentTooltipHelper.createName(enchantment, level));
    }
}