package outercloud.bol.goals;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import outercloud.bol.BossesOfLegend;

import java.util.HashMap;
import java.util.Map;

public class GoalSerializer {
    private static HashMap<Identifier, Class<? extends SerializableGoal>> registry = new HashMap<>();

    public static void register(Identifier identifier, Class<? extends SerializableGoal> goal) {
        registry.put(identifier, goal);
    }

    public static boolean registered(Goal goal) {
        Identifier identifier = ((SerializableGoal) goal).getIdentifier();

        return registry.containsKey(identifier);
    }

    public static boolean registered(PrioritizedGoal prioritizedGoal) {
        return registered(prioritizedGoal.getGoal());
    }

    public static NbtCompound serialize(PrioritizedGoal prioritizedGoal) {
        NbtCompound data = ((SerializableGoal) prioritizedGoal.getGoal()).serialize();
        data.putInt("priority", prioritizedGoal.getPriority());

        Identifier identifier = ((SerializableGoal) prioritizedGoal.getGoal()).getIdentifier();

        data.putString("identifier", identifier.toString());

        return  data;
    }

    public static PrioritizedGoal deserialize(NbtCompound nbt) {
        Identifier identifier = new Identifier(nbt.getString("identifier"));

        Map.Entry<Identifier, Class<? extends SerializableGoal>> match = registry.entrySet().stream().filter(entry -> entry.getKey().equals(identifier)).findFirst().orElse(null);

        if(match == null) {
            BossesOfLegend.LOGGER.error("Could not deserialize goal with identifier " + identifier + " because it has not been registered!");

            return null;
        }

        Goal goal = null;

        try {
            goal = (Goal) match.getValue().newInstance();
        } catch (InstantiationException | IllegalAccessException ignored) {}

        if(goal == null) {
            BossesOfLegend.LOGGER.error("Could not deserialize goal with identifier " + identifier + " because it failed to instantiate!");

            return null;
        }

        return  new PrioritizedGoal(nbt.getInt("priority"), goal);
    }
}
