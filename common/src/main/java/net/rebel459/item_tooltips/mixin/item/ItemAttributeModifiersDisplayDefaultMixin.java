package net.rebel459.item_tooltips.mixin.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.rebel459.item_tooltips.config.ITConfig;
import net.rebel459.item_tooltips.tag.ITItemTags;
import net.rebel459.item_tooltips.util.MiningTooltipHelper;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemAttributeModifiers.Display.Default.class)
public class ItemAttributeModifiersDisplayDefaultMixin {

    @Inject(method = "apply", at = @At(value = "TAIL"))
    private void addMiningSpeedTooltip(Consumer<Component> consumer, @Nullable Player player, Holder<Attribute> attribute, AttributeModifier modifier, CallbackInfo ci) {
        if (!ITConfig.get().items.mining_speed_tooltip) return;
        ItemStack stack = MiningTooltipHelper.getStack();
        if (!attribute.is(Attributes.ATTACK_SPEED) || !modifier.is(Item.BASE_ATTACK_SPEED_ID) || stack == null || !stack.has(DataComponents.TOOL) || stack.is(ITItemTags.HIDDEN_MINING_SPEED)) return;

        Tool tool = stack.get(DataComponents.TOOL);
        float miningSpeed = tool.rules().stream()
                .filter(rule -> rule.correctForDrops().orElse(false))
                .flatMap(rule -> rule.speed().stream())
                .max(Float::compare)
                .orElse(tool.defaultMiningSpeed());

        if (miningSpeed == 1F) return;

        consumer.accept(
                Component.literal(" ")
                        .append(Component.literal(ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(miningSpeed) + " "))
                        .append(Component.translatable("tooltip.item_tooltips.mining_speed"))
                        .withStyle(ChatFormatting.DARK_GREEN)
        );
    }
}
