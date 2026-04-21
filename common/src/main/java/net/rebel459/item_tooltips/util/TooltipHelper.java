package net.rebel459.item_tooltips.util;

import me.shedaniel.clothconfig2.gui.AbstractConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.rebel459.item_tooltips.ItemTooltips;
import net.rebel459.item_tooltips.config.ITConfig;
import net.rebel459.item_tooltips.mixin.client.ClientTextTooltipAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TooltipHelper {

    public static boolean shouldWrapText(Minecraft instance) {
        return ITConfig.get().tooltips.wrap_text &&
                instance != null && (
                instance.screen instanceof AbstractConfigScreen || (
                        instance.player != null && (
                                instance.screen == null || (
                                        instance.screen.isInGameUi() &&
                                                !instance.screen.isPauseScreen() &&
                                                !(instance.screen instanceof ChatScreen)
                                )
                        )
                )
        );
    }

    static final FontDescription.Resource INDENT_FONT = new FontDescription.Resource(ItemTooltips.id("indents"));

    static final char[] SPACE_CHARS = {
            '\uE000', // +1
            '\uE001', // +2
            '\uE002', // +4
            '\uE003', // +8
            '\uE004', // +16
            '\uE005', // +32
            '\uE006', // +64
            '\uE007', // +128
            '\uE008'  // +256
    };
    static final int[] SPACE_ADVANCES = {1, 2, 4, 8, 16, 32, 64, 128, 256};

    public static int getAllowedMaxWidth(int screenWidth) {
        int length = ITConfig.get().tooltips.length;
        int lengthCap = Math.clamp(ITConfig.get().tooltips.length_cap, 10, 100);

        int allowedMaxWidth;
        if (length > screenWidth / 100 * lengthCap || length == -1) allowedMaxWidth = screenWidth / 100 * lengthCap;
        else allowedMaxWidth = length;
        return allowedMaxWidth;
    }

    public static List<FormattedCharSequence> wrapTooltipLines(int screenWidth, int screenHeight, Font textRenderer, List<? extends Component> lines) {
        if (lines.stream().allMatch(text -> text.getString().isBlank()))
            return List.of();

        int maxWidth = getMaxWidth(textRenderer, lines);
        int allowedMaxWidth = getAllowedMaxWidth(screenWidth);

        if (maxWidth <= allowedMaxWidth)
            return lines.stream().map(Component::getVisualOrderText).collect(Collectors.toList());

        String descriptionPrefix = ITConfig.get().descriptions.prefix.text;
        String enchantmentPrefix = ITConfig.get().enchantments.prefix.text;

        if (!ITConfig.get().descriptions.prefix.align_wrapped_text) descriptionPrefix = "";
        if (!ITConfig.get().enchantments.prefix.align_wrapped_text) enchantmentPrefix = "";

        int descriptionPrefixWidth = textRenderer.width(descriptionPrefix);
        int enchantmentPrefixWidth = textRenderer.width(enchantmentPrefix);

        java.util.function.IntFunction<String> buildIndent = width -> {
            if (width <= 0) return "";
            StringBuilder sb = new StringBuilder();
            int remaining = width;
            for (int i = SPACE_ADVANCES.length - 1; i >= 0; i--) {
                if (remaining >= SPACE_ADVANCES[i]) {
                    sb.append(SPACE_CHARS[i]);
                    remaining -= SPACE_ADVANCES[i];
                }
            }
            return sb.toString();
        };

        List<FormattedCharSequence> wrapped = new ArrayList<>();
        for (Component line : lines) {
            String lineStr = line.getString();

            boolean hasDescriptionPrefix = !descriptionPrefix.isEmpty() && lineStr.startsWith(descriptionPrefix);
            boolean hasEnchantmentPrefix = !enchantmentPrefix.isEmpty() && lineStr.startsWith(enchantmentPrefix);

            int thisPrefixWidth = 0;
            if (hasDescriptionPrefix) {
                thisPrefixWidth = descriptionPrefixWidth;
            } else if (hasEnchantmentPrefix) {
                thisPrefixWidth = enchantmentPrefixWidth;
            }

            int splitWidth = Math.max(allowedMaxWidth - thisPrefixWidth, 1);
            List<FormattedCharSequence> splitLines = textRenderer.split(line, splitWidth);

            for (int i = 0; i < splitLines.size(); i++) {
                FormattedCharSequence part = splitLines.get(i);

                if (i == 0 || thisPrefixWidth == 0) {
                    wrapped.add(part);
                } else {
                    String indentStr = buildIndent.apply(thisPrefixWidth);
                    if (!indentStr.isEmpty()) {
                        Style indentStyle = line.getStyle().withFont(INDENT_FONT);
                        FormattedCharSequence indentSeq = FormattedCharSequence.forward(indentStr, indentStyle);
                        wrapped.add(FormattedCharSequence.composite(indentSeq, part));
                    } else {
                        wrapped.add(part);
                    }
                }
            }
        }

        return wrapped;
    }



    public static List<ClientTooltipComponent> wrapComponents(List<ClientTooltipComponent> components, Font font, int screenWidth, int screenHeight) {
        List<ClientTooltipComponent> wrapped = new ArrayList<>();
        List<Component> groupedText = new ArrayList<>();

        for (ClientTooltipComponent component : components) {
            if (component instanceof ClientTextTooltip textTooltip) {
                FormattedCharSequence charSequence = ((ClientTextTooltipAccessor) textTooltip).getText();
                Component text = toText(charSequence);
                groupedText.add(text);
            } else {
                if (!groupedText.isEmpty()) {
                    wrapped.addAll(convertComponentToTooltip(groupedText, font, screenWidth, screenHeight));
                    groupedText.clear();
                }

                wrapped.add(component);
            }
        }
        if (!groupedText.isEmpty()) {
            wrapped.addAll(convertComponentToTooltip(groupedText, font, screenWidth, screenHeight));
            groupedText.clear();
        }

        return wrapped;
    }

    private static List<ClientTextTooltip> convertComponentToTooltip(List<Component> lines, Font font, int screenWidth, int screenHeight) {
        return wrapTooltipLines(screenWidth, screenHeight, font, lines).stream()
                .map(ClientTextTooltip::new)
                .toList();
    }

    public static int getMaxWidth(Font textRenderer, List<? extends Component> lines) {
        int maxWidth = 0;

        for (Component line : lines) {
            int width = textRenderer.width(line);
            if (width > maxWidth)
                maxWidth = width;
        }

        return maxWidth;
    }

    public static MutableComponent toText(FormattedCharSequence charSequence) {
        MutableComponent text = Component.empty();

        StringBuilder builder = new StringBuilder();
        final Style[] prevStyle = {Style.EMPTY};
        charSequence.accept((idx, style, codePoint) -> {
            if (!style.equals(prevStyle[0])) {
                if (!builder.isEmpty()) {
                    text.append(Component.literal(builder.toString()).setStyle(prevStyle[0]));
                    builder.setLength(0);
                }
                prevStyle[0] = style;
            }
            builder.appendCodePoint(codePoint);

            return true;
        });
        if (!builder.isEmpty()) {
            text.append(Component.literal(builder.toString()).setStyle(prevStyle[0]));
        }

        return text;
    }
}
