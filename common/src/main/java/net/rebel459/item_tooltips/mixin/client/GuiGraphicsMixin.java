package net.rebel459.item_tooltips.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.rebel459.item_tooltips.util.TooltipHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsMixin {

    @Shadow public abstract int guiWidth();

    @Shadow public abstract int guiHeight();

    @Shadow
    @Final
    private Minecraft minecraft;

    @ModifyVariable(method = "tooltip", at = @At("HEAD"), argsOnly = true)
    private List<ClientTooltipComponent> modifyTooltip(List<ClientTooltipComponent> tooltip, Font font, List<ClientTooltipComponent> list, int x, int y, ClientTooltipPositioner positioner) {
        if (TooltipHelper.shouldWrapText(this.minecraft)) return TooltipHelper.wrapComponents(tooltip, font, this.guiWidth(), this.guiHeight());
        return list;
    }
}
