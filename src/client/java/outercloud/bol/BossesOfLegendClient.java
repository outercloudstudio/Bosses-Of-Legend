package outercloud.bol;

import net.fabricmc.api.ClientModInitializer;

public class BossesOfLegendClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BossScreen.register();
	}
}