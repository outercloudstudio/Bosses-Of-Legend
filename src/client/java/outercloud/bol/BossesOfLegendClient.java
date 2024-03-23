package outercloud.bol;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import outercloud.bol.goals.GoalUi;
import outercloud.bol.goals.InflictEffectGoal;

public class BossesOfLegendClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BossScreen.register();

		GoalUi.register(InflictEffectGoal.IDENTIFIER, (data) -> {
			TextFieldWidget textFieldWidget = new TextFieldWidget(data.screen.getTextRenderer(), 0, 0, 128, 16, Text.of(""));
			textFieldWidget.setX(data.screen.width / 2 - textFieldWidget.getWidth() / 2);
			textFieldWidget.setY(data.screen.height / 2 - textFieldWidget.getHeight() / 2);
			textFieldWidget.setText(data.nbt.getCompound("data").getString("effect"));

			textFieldWidget.setChangedListener(text -> {
				data.nbt.getCompound("data").putString("effect", text);

				data.screen.editGoal(data.nbt.getCompound("data"), data.index);
			});

			data.screen.addOpenGoalElement(textFieldWidget);


		});
	}
}