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
import net.rebel459.item_tooltips.util.MiningTooltipHelper;
import net.rebel459.item_tooltips.util.ScreenHelper;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
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

    @Shadow
    public abstract int getBarColor();

    @Unique
    public boolean displayedShiftNotice = false;

    @Unique
    private boolean isTag(TagKey<Item> tag) {
        return this.is(item -> item.is(tag));
    }

    @Inject(method = "addDetailsToTooltip", at = @At(value = "HEAD"))
    private void addDescription(Item.TooltipContext context, TooltipDisplay display, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder, CallbackInfo ci) {
        ITConfig.DescriptionConfig descriptionConfig = ITConfig.get().descriptions;
        if (!descriptionConfig.add_descriptions || this.isTag(ITItemTags.DESCRIPTION_BLACKLIST)) return;
        if (this.isTag(ITItemTags.HAS_DESCRIPTION)) {
            MutableComponent prefixText = Component.translatable(descriptionConfig.prefix.text).withColor(descriptionConfig.prefix.color);
            MutableComponent descriptionText = Component.translatable(this.getItem().getDescriptionId() + ".desc").withColor(descriptionConfig.color);
            if (descriptionConfig.require_key_hold) {
                if (ScreenHelper.Tooltip.hasKeyDown()) {
                    builder.accept(Component.literal("").append(prefixText).append(descriptionText));
                    this.displayedShiftNotice = false;
                }
                else if (descriptionConfig.key_hold_notice && !this.isTag(ITItemTags.NO_DESCRIPTION_NOTICE)) {
                    builder.accept(Component.translatable("tooltip." + ItemTooltips.MOD_ID + ".hold_" + ScreenHelper.Tooltip.getString()).withColor(descriptionConfig.color));
                    this.displayedShiftNotice = true;
                }
            }
            else {
                builder.accept(Component.literal("").append(prefixText).append(descriptionText));
                this.displayedShiftNotice = false;
            }
        }
    }

    @Inject(method = "addDetailsToTooltip", at = @At(value = "HEAD"))
    private void addEnchantmentShiftNotice(Item.TooltipContext context, TooltipDisplay display, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder, CallbackInfo ci) {
        if (!ItemTooltips.enchantmentTooltips || !ITConfig.get().enchantments.require_key_hold || !ITConfig.get().enchantments.key_hold_notice || (!this.isEnchanted() && !this.getComponents().has(DataComponents.STORED_ENCHANTMENTS))) return;
        if (!ScreenHelper.Tooltip.hasKeyDown() && !this.displayedShiftNotice) builder.accept(Component.translatable("tooltip." + ItemTooltips.MOD_ID + ".hold_" + ScreenHelper.Tooltip.getString()).withColor(ITConfig.get().descriptions.color));
    }

    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    private void addEnchantmentDescription(Item.TooltipContext context, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
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

    @ModifyArg(
            method = "lambda$addAttributeTooltips$0",
            at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 0),
            index = 0
    )
    private static Object retainAttributeEmptySpace(Object component) {
        return ITConfig.get().tooltips.retain_empty_space ? Component.literal(" ") : component;
    }

    @Inject(method = "addAttributeTooltips", at = @At("HEAD"))
    private void beginAttributeTooltip(Consumer<Component> consumer, TooltipDisplay display, @Nullable Player player, CallbackInfo ci) {
        MiningTooltipHelper.setStack(ItemStack.class.cast(this));
    }

    @Inject(method = "addAttributeTooltips", at = @At("TAIL"))
    private void endAttributeTooltip(Consumer<Component> consumer, TooltipDisplay display, @Nullable Player player, CallbackInfo ci) {
        MiningTooltipHelper.clear();
    }


    @Inject(method = "addDetailsToTooltip", at = @At(value = "TAIL"))
    private void addDurability(Item.TooltipContext context, TooltipDisplay display, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder, CallbackInfo ci) {
        ItemStack stack = ItemStack.class.cast(this);
        if (!stack.has(DataComponents.MAX_DAMAGE) || stack.getMaxDamage() == 0 || stack.is(ITItemTags.NO_DURABILITY_TOOLTIP)) return;
        ITConfig.ItemConfig.DurabilityConfig durabilityConfig = ITConfig.get().items.durability;
        if (durabilityConfig.durability_tooltip == ITConfig.DurabilityTooltip.NONE) return;

        boolean hasKeyDown = ScreenHelper.hasKeyDown();
        int baseColor = durabilityConfig.color;
        int durabilityColor = baseColor;
        int maxDurability = stack.getMaxDamage();
        int durability = maxDurability - stack.getDamageValue();
        boolean hasFullDurability = durability >= maxDurability;
        if (!hasFullDurability) durabilityColor = stack.getBarColor();

        boolean showTooltip = false;
        if (durabilityConfig.durability_tooltip == ITConfig.DurabilityTooltip.HOLD_KEY && hasKeyDown) showTooltip = true;
        else if (durabilityConfig.durability_tooltip == ITConfig.DurabilityTooltip.ALWAYS) showTooltip = true;
        else if (durabilityConfig.durability_tooltip == ITConfig.DurabilityTooltip.DAMAGED && !hasFullDurability) showTooltip = true;
        if (!showTooltip) return;

        MutableComponent durabilityTooltip = (Component.literal("")
                .append(Component.translatable("tooltip." + ItemTooltips.MOD_ID + ".durability").withColor(baseColor)))
                .append(Component.literal(": ").withColor(baseColor))
                .append(Component.literal(String.valueOf(durability)).withColor(durabilityColor));

        boolean showMaxTooltip = false;
        if (durabilityConfig.max_durability_tooltip == ITConfig.DurabilityTooltip.HOLD_KEY && hasKeyDown) showMaxTooltip = true;
        else if (durabilityConfig.max_durability_tooltip == ITConfig.DurabilityTooltip.ALWAYS) showMaxTooltip = true;
        else if (durabilityConfig.max_durability_tooltip == ITConfig.DurabilityTooltip.DAMAGED && !hasFullDurability) showMaxTooltip = true;
        if (showMaxTooltip) {
            durabilityTooltip
                    .append(Component.literal(durabilityConfig.division_text).withColor(baseColor))
                    .append(Component.literal(String.valueOf(maxDurability)).withColor(baseColor));
        }

        builder.accept(durabilityTooltip);
    }
}
