package outercloud.bol.goals;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import outercloud.bol.BossesOfLegend;

import java.util.HashMap;
import java.util.function.BiFunction;

public class GoalDeserializers {
    private static HashMap<Identifier, BiFunction<MobEntity, NbtCompound, SerializableGoal>> deserializerRegistry = new HashMap<>();

    public static void register(Identifier identifier, BiFunction<MobEntity, NbtCompound, SerializableGoal> deserializer) {
        deserializerRegistry.put(identifier, deserializer);
    }

    public static NbtCompound serialize(PrioritizedGoal prioritizedGoal) {
        NbtCompound data = ((SerializableGoal) prioritizedGoal.getGoal()).serialize();
        data.putInt("priority", prioritizedGoal.getPriority());

        Identifier identifier = ((SerializableGoal) prioritizedGoal.getGoal()).getIdentifier();

        data.putString("identifier", identifier.toString());

        return  data;
    }

    public static PrioritizedGoal deserialize(MobEntity mobEntity, NbtCompound nbt) {
        Identifier identifier = new Identifier(nbt.getString("identifier"));

        if(!deserializerRegistry.containsKey(identifier)) {
            BossesOfLegend.LOGGER.error("Could not deserialize goal with identifier " + identifier + " because it has not been registered!");

            return null;
        }

        BiFunction<MobEntity, NbtCompound, SerializableGoal> deserializer = deserializerRegistry.get(identifier);

        Goal goal = (Goal) deserializer.apply(mobEntity, nbt);

        return  new PrioritizedGoal(nbt.getInt("priority"), goal);
    }
}
