package net.rebel459.item_tooltips.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfigClient;

public final class ITModMenuIntegration implements ModMenuApi {

    @Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfigClient.getConfigScreen(ITConfig.class, parent).get();
	}
}