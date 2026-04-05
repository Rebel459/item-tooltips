package net.rebel459.item_tooltips.mixin.item;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.rebel459.item_tooltips.ItemTooltips;
import net.rebel459.item_tooltips.config.ITConfig;
import net.rebel459.item_tooltips.tag.ITItemTags;
import net.rebel459.item_tooltips.util.ScreenHelper;
import net.rebel459.item_tooltips.util.TooltipHelper;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract boolean isEnchanted();

    @Shadow
    public abstract DataComponentMap getComponents();

    @Shadow
    public abstract boolean is(Predicate<Holder<Item>> item);

    @Unique
    public boolean displayedShiftNotice = false;

    @Unique
    private boolean isTag(TagKey<Item> tag) {
        return this.is(item -> item.is(tag));
    }

    @Inject(method = "addDetailsToTooltip", at = @At(value = "HEAD"))
    private void addDescription(Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Player player, TooltipFlag tooltipFlag, Consumer<Component> consumer, CallbackInfo ci) {
        if (!ITConfig.get().descriptions.add_descriptions || this.isTag(ITItemTags.DESCRIPTION_BLACKLIST)) return;
        if (this.isTag(ITItemTags.HAS_DESCRIPTION)) {
            MutableComponent prefixText = Component.translatable(ITConfig.get().descriptions.prefix.text).withColor(ITConfig.get().descriptions.prefix.color);
            MutableComponent descriptionText = Component.translatable(this.getItem().getDescriptionId() + ".desc").withColor(ITConfig.get().descriptions.color);
            if (ITConfig.get().descriptions.require_key_hold) {
                if (ScreenHelper.Tooltip.hasKeyDown()) {
                    consumer.accept(Component.literal("").append(prefixText).append(descriptionText));
                    this.displayedShiftNotice = false;
                }
                else if (ITConfig.get().descriptions.key_hold_notice && !this.isTag(ITItemTags.NO_DESCRIPTION_NOTICE)) {
                    consumer.accept(Component.translatable("tooltip." + ItemTooltips.MOD_ID + ".hold_" + ScreenHelper.Tooltip.getString()).withColor(ITConfig.get().descriptions.color));
                    this.displayedShiftNotice = true;
                }
            }
            else {
                consumer.accept(Component.literal("").append(prefixText).append(descriptionText));
                this.displayedShiftNotice = false;
            }
        }
    }

    @Inject(method = "addDetailsToTooltip", at = @At(value = "HEAD"))
    private void addEnchantmentShiftNotice(Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Player player, TooltipFlag tooltipFlag, Consumer<Component> consumer, CallbackInfo ci) {
        if (!ItemTooltips.enchantmentTooltips || !ITConfig.get().enchantments.require_key_hold || !ITConfig.get().enchantments.key_hold_notice || (!this.isEnchanted() && !this.getComponents().has(DataComponents.STORED_ENCHANTMENTS))) return;
        if (!ScreenHelper.Tooltip.hasKeyDown() && !this.displayedShiftNotice) consumer.accept(Component.translatable("tooltip." + ItemTooltips.MOD_ID + ".hold_" + ScreenHelper.Tooltip.getString()).withColor(ITConfig.get().descriptions.color));
    }

    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    private void addEnchantmentDescription(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        if (!ItemTooltips.enchantmentTooltips || (ITConfig.get().enchantments.require_key_hold && !ScreenHelper.Tooltip.hasKeyDown())) {
            return;
        }

        ItemStack stack = ItemStack.class.cast(this);
        List<Component> tooltip = cir.getReturnValue();

        HashSet<Object2IntMap.Entry<Holder<Enchantment>>> enchantments = new HashSet<>(stack.getEnchantments().entrySet());
        enchantments.addAll(stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).entrySet());
        if (enchantments.isEmpty()) return;

        for (Object2IntMap.Entry<Holder<Enchantment>> enchantmentEntry : enchantments) {
            Enchantment enchantment = enchantmentEntry.getKey().value();

            for (int x = 0; x < tooltip.size(); x++) {
                if (!tooltip.get(x).getContents().equals(enchantment.description().getContents())) continue;

                Identifier enchantmentId = enchantmentEntry.getKey().unwrapKey().get().identifier();
                MutableComponent description = (Component.literal("")
                        .append(Component.translatable(ITConfig.get().enchantments.prefix.text).withColor(ITConfig.get().enchantments.prefix.color))
                        .append(Component.translatable("enchantment." + enchantmentId.getNamespace() + "." + enchantmentId.getPath() + ".desc").withColor(ITConfig.get().enchantments.color)));

                tooltip.add(x + 1, description);
            }
        }
    }

    @Inject(method = "addAttributeTooltips", at = @At("HEAD"), cancellable = true)
    private void spacierAttributeTooltips(Consumer<Component> consumer, TooltipDisplay tooltipDisplay, @Nullable Player player, CallbackInfo ci) {
        if (!ITConfig.get().tooltips.retain_empty_space) return;
        ItemStack stack = ItemStack.class.cast(this);
        TooltipHelper.addAttributeTooltips(consumer, tooltipDisplay, player, stack);
        ci.cancel();
    }
}