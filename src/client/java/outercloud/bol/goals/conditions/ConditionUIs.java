package outercloud.bol.goals.conditions;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.util.TriConsumer;
import outercloud.bol.BossesOfLegend;
import outercloud.bol.goals.OpenGoalScreen;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class ConditionUIs {
    private static HashMap<Identifier, TriConsumer<OpenGoalScreen, NbtCompound, Integer>> registry = new HashMap<>();

    public static void register(Identifier identifier, TriConsumer<OpenGoalScreen, NbtCompound, Integer> ui) {
        registry.put(identifier, ui);
    }

    public static void create(OpenGoalScreen openGoalScreen, NbtCompound nbt, int index) {
        Identifier identifier = new Identifier(nbt.getString("identifier"));

        if(!registry.containsKey(identifier)) {
            BossesOfLegend.LOGGER.error("Could not deserialize condition ui with identifier " + identifier + " because it has not been registered!");
        }

        TriConsumer<OpenGoalScreen, NbtCompound, Integer> ui = registry.get(identifier);

        ui.accept(openGoalScreen, nbt, index);
    }
}
