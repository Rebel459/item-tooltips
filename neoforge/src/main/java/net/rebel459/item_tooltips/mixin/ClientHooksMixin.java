package net.rebel459.item_tooltips.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import net.rebel459.item_tooltips.config.ITConfig;
import net.rebel459.item_tooltips.util.TooltipHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ClientHooks.class)
public class ClientHooksMixin {

    @Inject(method = "gatherTooltipComponentsFromElements", at = @At("HEAD"), cancellable = true)
    private static void replaceTooltipWrapper(ItemStack stack, List<Either<FormattedText, TooltipComponent>> elements, int mouseX, int screenWidth, int screenHeight, Font fallbackFont, CallbackInfoReturnable<List<ClientTooltipComponent>> cir) {
        Minecraft minecraft =  Minecraft.getInstance();
        if (!ITConfig.get.tooltips.wrap_text || minecraft.player == null && minecraft.screen instanceof ChatScreen) return;

        List<ClientTooltipComponent> converted = elements.stream()
                .map(e -> e.map(
                        text -> ClientTooltipComponent.create(
                                text instanceof Component ? ((Component) text).getVisualOrderText() : Language.getInstance().getVisualOrder(text)
                        ),
                        ClientTooltipComponent::create
                ))
                .toList();

        List<ClientTooltipComponent> wrapped = TooltipHelper.wrapComponents(converted, fallbackFont, screenWidth, screenHeight);

        cir.setReturnValue(wrapped);
    }
}