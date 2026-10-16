package net.rebel459.item_tooltips.mixin.integration.jei;

import com.mojang.datafixers.util.Either;
import mezz.jei.fabric.platform.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.rebel459.item_tooltips.util.TooltipHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Ljava/util/List;IILnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private void addDescription(GuiGraphicsExtractor guiGraphics, List<Either<FormattedText, TooltipComponent>> elements, int x, int y, Font font, ItemStack stack, CallbackInfo ci) {
        if (!TooltipHelper.shouldWrapText(Minecraft.getInstance())) return;
        tooltip(guiGraphics, elements, x, y, font, DefaultTooltipPositioner.INSTANCE);
        ci.cancel();
    }

    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Ljava/util/List;IILnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", at = @At("HEAD"), cancellable = true)
    private void addDescriptionWithPositioner(GuiGraphicsExtractor guiGraphics, List<Either<FormattedText, TooltipComponent>> elements, int x, int y, Font font, ItemStack stack, ClientTooltipPositioner positioner, CallbackInfo ci) {
        if (!TooltipHelper.shouldWrapText(Minecraft.getInstance())) return;
        tooltip(guiGraphics, elements, x, y, font, positioner);
        ci.cancel();
    }

    @Unique
    private void tooltip(GuiGraphicsExtractor guiGraphics, List<Either<FormattedText, TooltipComponent>> elements, int x, int y, Font font, ClientTooltipPositioner positioner) {
        List<ClientTooltipComponent> components = elements.stream()
                .flatMap(either -> either.map(
                        formattedText -> {
                            FormattedCharSequence sequence = Language.getInstance().getVisualOrder(formattedText);
                            return Stream.of(ClientTooltipComponent.create(sequence));
                        },
                        tooltipComponent -> Stream.of(this.createClientTooltipComponent(tooltipComponent))
                ))
                .collect(Collectors.toCollection(ArrayList::new));
        guiGraphics.setTooltipForNextFrameInternal(font, components, x, y, positioner, null, true);
    }
}