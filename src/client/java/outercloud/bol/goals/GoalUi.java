package outercloud.bol.goals;

import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import outercloud.bol.BossScreen;

import java.util.HashMap;
import java.util.function.Consumer;

public class GoalUi {
    private static HashMap<Identifier, Consumer<GoalUiData>> registry = new HashMap<>();

    public static void register(Identifier identifier, Consumer<GoalUiData> ui) {
        registry.put(identifier, ui);
    }

    public static void create(BossScreen screen, NbtCompound nbt, int index) {
        Identifier identifier = new Identifier(nbt.getString("identifier"));

        if(!registry.containsKey(identifier)) {
            TextWidget textWidget = new TextWidget(Text.of("Can't edit this goal!"), screen.getTextRenderer());
            textWidget.setWidth(64);
            textWidget.setX(screen.width / 2 - textWidget.getWidth() / 2);
            textWidget.setY(screen.height / 2 - textWidget.getHeight() / 2);
            textWidget.alignCenter();

            screen.addOpenGoalElement(textWidget);

            return;
        }

        Consumer<GoalUiData> create = registry.get(identifier);

        GoalUiData data = new GoalUiData();
        data.screen = screen;
        data.nbt = nbt;
        data.index = index;

        create.accept(data);
    }
}
