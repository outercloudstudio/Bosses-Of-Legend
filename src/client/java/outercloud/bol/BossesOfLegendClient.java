package outercloud.bol;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import outercloud.bol.goals.OpenGoalUIs;
import outercloud.bol.goals.InflictEffectGoal;
import outercloud.bol.goals.conditions.ConditionDeserializers;
import outercloud.bol.goals.conditions.ConditionUIs;
import outercloud.bol.goals.conditions.HealthCondition;

import java.util.Arrays;

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

		ConditionUIs.register(HealthCondition.IDENTIFIER, (goalScreen, nbt, index) -> {
			goalScreen.add(new TextWidget(goalScreen.screen.width / 2 - 64, goalScreen.nextY + 1, 64, 18, Text.of("Health"), goalScreen.screen.getTextRenderer())).alignLeft();

			HealthCondition healthCondition = (HealthCondition) ConditionDeserializers.deserialize(nbt);

			ButtonWidget operatorButton = ButtonWidget.builder(Text.of(healthCondition.conditionToString()), widget -> {
				HealthCondition.Operator newOperator = HealthCondition.Operator.LessThan;

				if(healthCondition.operator == HealthCondition.Operator.LessThan) {
					newOperator = HealthCondition.Operator.LessThanEqual;
				} else if(healthCondition.operator == HealthCondition.Operator.LessThanEqual) {
					newOperator = HealthCondition.Operator.Equal;
				} else if(healthCondition.operator == HealthCondition.Operator.Equal) {
					newOperator = HealthCondition.Operator.GreaterThan;
				} else if(healthCondition.operator == HealthCondition.Operator.GreaterThan) {
					newOperator = HealthCondition.Operator.GreaterThanEqual;
				} else if(healthCondition.operator == HealthCondition.Operator.GreaterThanEqual) {
					newOperator = HealthCondition.Operator.LessThan;
				}

				healthCondition.operator = newOperator;

				widget.setMessage(Text.of(healthCondition.conditionToString()));

				goalScreen.nbt.getCompound("data").getList("conditionGroup", NbtElement.COMPOUND_TYPE).set(index, healthCondition.serialize());
				goalScreen.screen.editGoal(goalScreen.nbt.getCompound("data"), goalScreen.index);
			}).dimensions(goalScreen.screen.width / 2 - 64 + 36, goalScreen.nextY, 18, 18).build();

			goalScreen.add(operatorButton);

			TextFieldWidget healthInput = new TextFieldWidget(goalScreen.screen.getTextRenderer(), goalScreen.screen.width / 2 - 64 + 36 + 18 + 6, goalScreen.nextY + 1, 64, 16, Text.of(""));
			healthInput.setText(String.valueOf(healthCondition.limit));

			healthInput.setTextPredicate(text -> {
				if(text.isEmpty()) return true;

				if(text.contains("d")) return false;

				try {
					Float.parseFloat(text);

					return true;
				} catch(Exception exception) {
					return false;
				}
			});

			healthInput.setChangedListener(text -> {
				if(text.isEmpty())  text = "0";

				healthCondition.limit = Float.parseFloat(text);

				BossesOfLegend.LOGGER.info(String.valueOf(goalScreen.nbt));

				goalScreen.nbt.getCompound("data").getList("conditionGroup", NbtElement.COMPOUND_TYPE).set(index, healthCondition.serialize());
				goalScreen.screen.editGoal(goalScreen.nbt.getCompound("data"), goalScreen.index);
			});

			goalScreen.add(healthInput);

			goalScreen.nextY += 18;
		});
	}
}