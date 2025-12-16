package net.legacy.item_tooltips.util;

import net.legacy.item_tooltips.config.ITConfig;
import net.legacy.item_tooltips.mixin.client.ClientTextTooltipMixin;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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

        // Get prefixes from config
        String descriptionPrefix = ITConfig.get.descriptions.prefix;
        String enchantmentPrefix = ITConfig.get.enchantments.prefix;

        // Disable prefixes if wrapping is turned off
        if (!ITConfig.get.descriptions.prefix_wrapping) descriptionPrefix = "";
        if (!ITConfig.get.enchantments.prefix_wrapping) enchantmentPrefix = "";

        // Calculate prefix widths
        int descriptionPrefixWidth = textRenderer.width(descriptionPrefix);
        int enchantmentPrefixWidth = textRenderer.width(enchantmentPrefix);

        // Use the larger prefix width for adjustment to ensure consistent wrapping
        int prefixWidth = Math.max(descriptionPrefixWidth, enchantmentPrefixWidth);
        int adjustedMaxWidth = Math.max(allowedMaxWidth - prefixWidth, 0);

        List<FormattedCharSequence> wrapped = new ArrayList<>();
        for (Component line : lines) {
            // Check which prefix the line starts with
            boolean hasDescriptionPrefix = descriptionPrefix.length() > 0 && line.getString().startsWith(descriptionPrefix);
            boolean hasEnchantmentPrefix = enchantmentPrefix.length() > 0 && line.getString().startsWith(enchantmentPrefix);

            // Split the line using adjustedMaxWidth if a prefix will be added, otherwise use allowedMaxWidth
            int splitWidth = (hasDescriptionPrefix || hasEnchantmentPrefix) ? adjustedMaxWidth : allowedMaxWidth;
            List<FormattedCharSequence> splitLines = textRenderer.split(line, splitWidth);

            for (int i = 0; i < splitLines.size(); i++) {
                FormattedCharSequence splitLine = splitLines.get(i);
                if (i == 0) {
                    // Always add the first line without modification
                    wrapped.add(splitLine);
                } else if (hasDescriptionPrefix) {
                    // Add description prefix to continuation lines
                    FormattedCharSequence prefixedLine = FormattedCharSequence.composite(
                            FormattedCharSequence.forward(descriptionPrefix, line.getStyle().withColor(ITConfig.get.descriptions.prefix_color)),
                            splitLine
                    );
                    wrapped.add(prefixedLine);
                } else if (hasEnchantmentPrefix) {
                    // Add enchantment prefix to continuation lines
                    FormattedCharSequence prefixedLine = FormattedCharSequence.composite(
                            FormattedCharSequence.forward(enchantmentPrefix, line.getStyle().withColor(ITConfig.get.enchantments.prefix_color)),
                            splitLine
                    );
                    wrapped.add(prefixedLine);
                } else {
                    // Add continuation lines without a prefix if no match
                    wrapped.add(splitLine);
                }
            }
        }

        return wrapped;
    }

    public static List<ClientTooltipComponent> wrapComponents(List<ClientTooltipComponent> components, Font font, int screenWidth, int screenHeight, int x, ClientTooltipPositioner tooltipPositioner) {
        if (Minecraft.getInstance().player == null) return components;
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