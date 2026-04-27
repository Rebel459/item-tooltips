package net.rebel459.item_tooltips.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.rebel459.item_tooltips.config.ITConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public abstract class ItemStackFabricMixin {

    @ModifyArg(
            method = "lambda$addAttributeTooltips$0",
            at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 0),
            index = 0
    )
    private static Object retainAttributeEmptySpace(Object component) {
        return ITConfig.get().tooltips.retain_empty_space ? Component.literal(" ") : component;
    }
}
