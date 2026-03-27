package net.rebel459.item_tooltips;

import net.fabricmc.api.ClientModInitializer;

public class ItemTooltipsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ItemTooltips.init();
    }
}
