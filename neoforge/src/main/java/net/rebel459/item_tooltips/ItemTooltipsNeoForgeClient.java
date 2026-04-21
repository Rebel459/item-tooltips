package net.rebel459.item_tooltips;

import me.shedaniel.autoconfig.AutoConfigClient;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.rebel459.item_tooltips.config.ITConfig;
import net.rebel459.item_tooltips.util.TooltipHelper;

import java.util.List;

@Mod(value = ItemTooltips.MOD_ID, dist = Dist.CLIENT)
public class ItemTooltipsNeoForgeClient {

    public ItemTooltipsNeoForgeClient(IEventBus modEventBus) {
        ItemTooltips.init();
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (modContainer, parent) ->
                        AutoConfigClient.getConfigScreen(ITConfig.class, parent).get()
        );
    }
}