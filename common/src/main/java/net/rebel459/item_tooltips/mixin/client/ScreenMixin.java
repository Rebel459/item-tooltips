package net.rebel459.item_tooltips.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.rebel459.item_tooltips.util.ScreenHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Final @Shadow @Nullable protected Minecraft minecraft;

    @Inject(method = "tick", at = @At("HEAD"))
    private void hasShiftDown(CallbackInfo ci) {
        if (this.minecraft == null) return;
        ScreenHelper.hasShiftDown = this.minecraft.hasShiftDown();
        ScreenHelper.hasAltDown = this.minecraft.hasAltDown();
        ScreenHelper.hasControlDown = this.minecraft.hasControlDown();
    }
}
