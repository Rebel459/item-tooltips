package net.rebel459.item_tooltips.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.neoforge.common.util.AttributeUtil;
import net.rebel459.item_tooltips.config.ITConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AttributeUtil.class)
public abstract class AttributeUtilMixin {

    @WrapOperation(
            method = "applyModifierTooltips",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;empty()Lnet/minecraft/network/chat/MutableComponent;", ordinal = 0)
    )
    private static MutableComponent retainAttributeEmptySpace(Operation<MutableComponent> original) {
        return ITConfig.get().tooltips.retain_empty_space ? Component.literal(" ") : original.call();
    }
}
