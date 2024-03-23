package outercloud.bol;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import outercloud.bol.goals.GoalUi;
import outercloud.bol.goals.InflictEffectGoal;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BossesOfLegendClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BossScreen.register();

		GoalUi.register(InflictEffectGoal.IDENTIFIER, (data) -> {
			int nextElementY = 32;

			TextFieldWidget effectWidget = new TextFieldWidget(data.screen.getTextRenderer(), 0, 0, 128, 16, Text.of(""));
			effectWidget.setX(data.screen.width / 2 - effectWidget.getWidth() / 2);
			effectWidget.setY(nextElementY);
			effectWidget.setText(data.nbt.getCompound("data").getString("effect"));

			effectWidget.setChangedListener(text -> {
				data.nbt.getCompound("data").putString("effect", text);

				data.screen.editGoal(data.nbt.getCompound("data"), data.index);
			});

			data.screen.addOpenGoalElement(effectWidget);

			nextElementY += effectWidget.getHeight() + 4;

			TextFieldWidget priorityWidget = new TextFieldWidget(data.screen.getTextRenderer(), 0, 0, 32, 16, Text.of(""));
			priorityWidget.setX(data.screen.width / 2 - priorityWidget.getWidth() / 2);
			priorityWidget.setY(nextElementY);
			priorityWidget.setText(String.valueOf(data.nbt.getCompound("data").getInt("priority")));

			priorityWidget.setTextPredicate(text -> Arrays.stream(text.split("")).filter("0123456789"::contains).toList().size() == text.length());

			priorityWidget.setChangedListener(text -> {
				data.nbt.getCompound("data").putInt("priority", Integer.parseInt(text));

				data.screen.editGoal(data.nbt.getCompound("data"), data.index);
			});

			data.screen.addOpenGoalElement(priorityWidget);

			nextElementY += priorityWidget.getHeight() + 4;
		});
	}
}