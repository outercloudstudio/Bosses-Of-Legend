package outercloud.bol.goals;

import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.function.Consumer;

public class OpenGoalUIRegistry {
    private static HashMap<Identifier, Consumer<OpenGoalScreen>> registry = new HashMap<>();

    public static void register(Identifier identifier, Consumer<OpenGoalScreen> ui) {
        registry.put(identifier, ui);
    }

    public static void create(OpenGoalScreen openGoalScreen) {
        Identifier identifier = new Identifier(openGoalScreen.nbt.getString("identifier"));

        if(!registry.containsKey(identifier)) {
            TextWidget textWidget = new TextWidget(Text.of("Can't edit this goal!"), openGoalScreen.screen.getTextRenderer());
            textWidget.setWidth(64);
            textWidget.setX(openGoalScreen.screen.width / 2 - textWidget.getWidth() / 2);
            textWidget.setY(openGoalScreen.screen.height / 2 - textWidget.getHeight() / 2);
            textWidget.alignCenter();

            openGoalScreen.add(textWidget);

            return;
        }

        registry.get(identifier).accept(openGoalScreen);
    }
}
