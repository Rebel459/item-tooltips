package net.legacy.item_tooltips.mixin.client;

import net.legacy.item_tooltips.config.ITConfig;
import net.legacy.item_tooltips.util.TooltipWrapper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @Shadow public abstract int guiWidth();

    @Shadow public abstract int guiHeight();

    @ModifyVariable(method = "renderTooltipInternal", at = @At("HEAD"), argsOnly = true)
    private List<ClientTooltipComponent> modifyTooltip(List<ClientTooltipComponent> tooltip, Font font, List<ClientTooltipComponent> list, int x, int y, ClientTooltipPositioner positioner) {
        if (ITConfig.get.tooltips.wrap_text) return TooltipWrapper.wrapComponents(tooltip, font, this.guiWidth(), this.guiHeight(), x, positioner);
        return (list);
    }
}
