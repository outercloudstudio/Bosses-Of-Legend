package outercloud.bol;

import net.fabricmc.api.ClientModInitializer;
import outercloud.bol.goals.OpenGoalUIs;
import outercloud.bol.goals.InflictEffectGoal;
import outercloud.bol.goals.conditions.ConditionUIs;
import outercloud.bol.goals.conditions.HealthCondition;

public class BossesOfLegendClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BossScreen.register();

		OpenGoalUIs.register(InflictEffectGoal.IDENTIFIER, (goalScreen) -> {
			goalScreen.setTitle("Inflict Effect");

			goalScreen.addTextProperty("effect", "Effect");
			goalScreen.addIntProperty("duration", "Duration");
			goalScreen.addIntProperty("amplifier", "Amplifier");
			goalScreen.addIntProperty("chance", "Chance");
			goalScreen.addTextProperty("command", "Command");
			goalScreen.addIntProperty("priority", "Priority");

			goalScreen.addConditionGroup();
		});

		ConditionUIs.register(HealthCondition.IDENTIFIER, (goalScreen, nbt) -> {
			goalScreen.setTitle("TEST");
		});
	}
}