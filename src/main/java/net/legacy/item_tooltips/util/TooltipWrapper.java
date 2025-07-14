package net.legacy.item_tooltips.util;

import net.legacy.item_tooltips.config.ITConfig;
import net.legacy.item_tooltips.mixin.client.ClientTextTooltipMixin;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TooltipWrapper {
    public static List<FormattedCharSequence> wrapTooltipLines(int screenWidth, int screenHeight, Font textRenderer, List<? extends Component> lines, int x, ClientTooltipPositioner tooltipPositioner) {
        if (lines.stream().allMatch(text -> text.getString().isBlank()))
            return List.of();
        int maxWidth = getMaxWidth(textRenderer, lines);

        int allowedMaxWidth;

        int length = ITConfig.get.tooltips.length;

        int lengthCap = ITConfig.get.tooltips.length_cap;
        if (lengthCap > 100) lengthCap = 100;

        if (length > screenWidth / 100 * lengthCap || length == -1) allowedMaxWidth = screenWidth / 100 * lengthCap;
        else allowedMaxWidth = length;

        if (maxWidth <= allowedMaxWidth)
            return lines.stream().map(Component::getVisualOrderText).collect(Collectors.toList());

        List<FormattedCharSequence> wrapped = new ArrayList<>();
        for (Component line : lines) {
            wrapped.addAll(textRenderer.split(line, allowedMaxWidth));
        }

        return wrapped;
    }

    public static List<ClientTooltipComponent> wrapComponents(List<ClientTooltipComponent> components, Font font, int screenWidth, int screenHeight, int x, ClientTooltipPositioner tooltipPositioner) {
        List<ClientTooltipComponent> wrapped = new ArrayList<>();
        List<Component> groupedText = new ArrayList<>();

        for (ClientTooltipComponent component : components) {
            if (component instanceof ClientTextTooltip textTooltip) {
                FormattedCharSequence charSequence = ((ClientTextTooltipMixin) textTooltip).getText();
                Component text = TextUtil.toText(charSequence);
                groupedText.add(text);
            } else {
                if (!groupedText.isEmpty()) {
                    wrapped.addAll(convertComponentToTooltip(groupedText, font, screenWidth, screenHeight, x, tooltipPositioner));
                    groupedText.clear();
                }

                wrapped.add(component);
            }
        }
        if (!groupedText.isEmpty()) {
            wrapped.addAll(convertComponentToTooltip(groupedText, font, screenWidth, screenHeight, x, tooltipPositioner));
            groupedText.clear();
        }

        return wrapped;
    }

    private static List<ClientTextTooltip> convertComponentToTooltip(List<Component> lines, Font font, int screenWidth, int screenHeight, int x, ClientTooltipPositioner tooltipPositioner) {
        return wrapTooltipLines(screenWidth, screenHeight, font, lines, x, tooltipPositioner).stream()
                .map(ClientTextTooltip::new)
                .toList();
    }

    private static int getMaxWidth(Font textRenderer, List<? extends Component> lines) {
        int maxWidth = 0;

        for (Component line : lines) {
            int width = textRenderer.width(line);
            if (width > maxWidth)
                maxWidth = width;
        }

        return maxWidth;
    }
}