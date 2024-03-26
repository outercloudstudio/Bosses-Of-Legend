package outercloud.bol;

import net.fabricmc.api.ClientModInitializer;
import outercloud.bol.goals.OpenGoalUIRegistry;
import outercloud.bol.goals.InflictEffectGoal;

public class BossesOfLegendClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BossScreen.register();

		OpenGoalUIRegistry.register(InflictEffectGoal.IDENTIFIER, (goalScreen) -> {
			goalScreen.setTitle("Inflict Effect");

			goalScreen.addTextProperty("effect", "Effect");
			goalScreen.addIntProperty("duration", "Duration");
			goalScreen.addIntProperty("amplifier", "Amplifier");
			goalScreen.addIntProperty("chance", "Chance");
			goalScreen.addTextProperty("command", "Command");
			goalScreen.addIntProperty("priority", "Priority");
		});
	}
}