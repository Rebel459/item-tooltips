package net.rebel459.item_tooltips.mixin.client;

import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.rebel459.item_tooltips.util.TooltipHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(AbstractConfigEntry.class)
public abstract class AbstractConfigEntryMixin {

    @Inject(method = "wrapLines", at = @At("HEAD"), cancellable = true)
    private void modifyTooltip(Component[] lines, int width, CallbackInfoReturnable<FormattedCharSequence[]> cir) {
        if (TooltipHelper.shouldWrapText(Minecraft.getInstance())) {
            FormattedCharSequence[] unwrapped = Arrays.stream(lines)
                    .map(Component::getVisualOrderText)
                    .toArray(FormattedCharSequence[]::new);
            cir.setReturnValue(unwrapped);
        }
    }
}
