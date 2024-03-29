package outercloud.bol;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import outercloud.bol.goals.OpenGoalUIs;
import outercloud.bol.goals.InflictEffectGoal;
import outercloud.bol.goals.conditions.ConditionDeserializers;
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
			goalScreen.add(new TextWidget(goalScreen.screen.width / 2 - 64, goalScreen.nextY + 1, 64, 18, Text.of("Health"), goalScreen.screen.getTextRenderer())).alignLeft();

			HealthCondition healthCondition = (HealthCondition) ConditionDeserializers.deserialize(nbt);

			ButtonWidget operatorButton = ButtonWidget.builder(Text.of(healthCondition.conditionToString()), widget -> {}).dimensions(goalScreen.screen.width / 2 - 64 + 36, goalScreen.nextY, 18, 18).build();

			goalScreen.add(operatorButton);

			goalScreen.nextY += 18;
		});
	}
}