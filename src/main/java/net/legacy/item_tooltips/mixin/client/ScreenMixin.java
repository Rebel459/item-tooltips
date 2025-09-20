package net.legacy.item_tooltips.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.legacy.item_tooltips.util.ScreenAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Shadow @Nullable protected Minecraft minecraft;

    @Inject(method = "tick", at = @At("HEAD"))
    private void hasShiftDown(CallbackInfo ci) {
        if (this.minecraft == null) return;
        ScreenAPI.hasShiftDown = this.minecraft.hasShiftDown();
        ScreenAPI.hasAltDown = this.minecraft.hasAltDown();
        ScreenAPI.hasControlDown = this.minecraft.hasControlDown();
    }
}
