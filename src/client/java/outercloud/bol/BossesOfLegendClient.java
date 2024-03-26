package outercloud.bol;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import outercloud.bol.goals.GoalUi;
import outercloud.bol.goals.InflictEffectGoal;

import java.util.Arrays;

public class BossesOfLegendClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BossScreen.register();

		GoalUi.register(InflictEffectGoal.IDENTIFIER, (data) -> {
			int nextElementY = 32;

			data.screen.addOpenGoalElement(new TextWidget(data.screen.width / 2 - 60, 12, 120, 18, Text.of("Inflict Effect"), data.screen.getTextRenderer()));

			TextWidget labelWidget = data.screen.addOpenGoalElement(new TextWidget(data.screen.width / 2 - 128 - 64, nextElementY, 120, 18, Text.of("Effect:"), data.screen.getTextRenderer()));
			labelWidget.alignRight();

			TextFieldWidget effectWidget = new TextFieldWidget(data.screen.getTextRenderer(), data.screen.width / 2 - 64, nextElementY, 128, 16, Text.of(""));
			effectWidget.setText(data.nbt.getCompound("data").getString("effect"));

			effectWidget.setChangedListener(text -> {
				data.nbt.getCompound("data").putString("effect", text);

				data.screen.editGoal(data.nbt.getCompound("data"), data.index);
			});

			data.screen.addOpenGoalElement(effectWidget);

			nextElementY += effectWidget.getHeight() + 4;

			labelWidget = data.screen.addOpenGoalElement(new TextWidget(data.screen.width / 2 - 128 - 64, nextElementY, 120, 18, Text.of("Priority:"), data.screen.getTextRenderer()));
			labelWidget.alignRight();

			TextFieldWidget priorityWidget = new TextFieldWidget(data.screen.getTextRenderer(), data.screen.width / 2 - 64, nextElementY, 128, 16, Text.of(""));
			priorityWidget.setText(String.valueOf(data.nbt.getCompound("data").getInt("priority")));

			priorityWidget.setTextPredicate(text -> text.isEmpty() || Arrays.stream(text.split("")).filter("0123456789"::contains).toList().size() == text.length());

			priorityWidget.setChangedListener(text -> {
				if(text.isEmpty()) {
					priorityWidget.setText("0");

					return;
				}

				data.nbt.getCompound("data").putInt("priority", Integer.parseInt(text));

				data.screen.editGoal(data.nbt.getCompound("data"), data.index);
			});

			data.screen.addOpenGoalElement(priorityWidget);

			nextElementY += priorityWidget.getHeight() + 4;

			labelWidget = data.screen.addOpenGoalElement(new TextWidget(data.screen.width / 2 - 128 - 64, nextElementY, 120, 18, Text.of("Command:"), data.screen.getTextRenderer()));
			labelWidget.alignRight();

			TextFieldWidget commandWidget = new TextFieldWidget(data.screen.getTextRenderer(), data.screen.width / 2 - 64, nextElementY, 128, 16, Text.of(""));
			commandWidget.setText(String.valueOf(data.nbt.getCompound("data").getString("command")));

			commandWidget.setChangedListener(text -> {
				data.nbt.getCompound("data").putString("command", text);

				data.screen.editGoal(data.nbt.getCompound("data"), data.index);
			});

			data.screen.addOpenGoalElement(commandWidget);

			nextElementY += commandWidget.getHeight() + 4;
		});
	}
}