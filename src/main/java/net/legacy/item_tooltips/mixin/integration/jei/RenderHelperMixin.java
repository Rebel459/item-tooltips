package net.legacy.item_tooltips.mixin.integration.jei;

import com.mojang.datafixers.util.Either;
import mezz.jei.fabric.platform.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.legacy.item_tooltips.config.ITConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
@Mixin(RenderHelper.class)
public abstract class RenderHelperMixin {

    @Shadow
    protected abstract ClientTooltipComponent createClientTooltipComponent(TooltipComponent tooltipComponent);

    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    private void addDescription(GuiGraphics guiGraphics, List<Either<FormattedText, TooltipComponent>> elements, int x, int y, Font font, ItemStack stack, CallbackInfo ci) {
        if (!ITConfig.get.tooltips.wrap_text) return;
        List<ClientTooltipComponent> components = elements.stream()
                .flatMap(either -> either.map(
                        formattedText -> {
                            FormattedCharSequence sequence = Language.getInstance().getVisualOrder(formattedText);
                            return Stream.of(ClientTooltipComponent.create(sequence));
                        },
                        tooltipComponent -> Stream.of(this.createClientTooltipComponent(tooltipComponent))
                ))
                .collect(Collectors.toCollection(ArrayList::new));
        guiGraphics.setTooltipForNextFrameInternal(font, components, x, y, DefaultTooltipPositioner.INSTANCE, null, true);
        ci.cancel();
    }
}
